package axle.ml

import axle._
import collection._
import FeatureNormalizer._

object KMeans extends KMeans()

/**
 * KMeans
 *
 */

trait KMeans {

  import axle.matrix.JblasMatrixFactory._ // TODO: generalize
  type M[T] = JblasMatrix[T]

  import DistanceFunction._

  /**
   * cluster[T]
   *
   * @tparam T  type of the objects being classified
   *
   * @param data
   * @param N
   * @param featureExtractor
   * @param constructor
   *
   */

  def cluster[T](data: Seq[T],
    N: Int,
    featureExtractor: T => Seq[Double],
    constructor: Seq[Double] => T,
    distance: DistanceFunction = EuclideanDistanceFunction,
    K: Int,
    iterations: Int): KMeansClassifier[T] = {

    val X = matrix(N, data.length, data.flatMap(featureExtractor(_)).toArray).t

    val normalizer = new PCAFeatureNormalizer(X, 0.95)
    val nd = normalizer.normalizedData()
    val μads = clusterLA(nd, distance, K, iterations)

    val (μ, a, d) = μads.last

    val assignmentLog = μads.map(_._2)
    val distanceLog = μads.map(_._3)

    KMeansClassifier(N, featureExtractor, constructor, μ, normalizer, distance, a, assignmentLog, distanceLog)
  }

  /**
   * centroidIndexAndDistanceClosestTo
   *
   * @param μ
   * @param x
   */

  def centroidIndexAndDistanceClosestTo(distance: DistanceFunction, μ: M[Double], x: M[Double]): (Int, Double) =
    (0 until μ.rows).map(r => (r, distance(μ.row(r), x))).minBy(_._2)

  /**
   * assignmentsAndDistances
   *
   * @param X
   * @param μ
   *
   * Returns:
   * N x 1 matrix: indexes of centroids closest to xi
   * N x 1 matrix: distances to those centroids
   */

  def assignmentsAndDistances(distance: DistanceFunction, X: M[Double], μ: M[Double]): (M[Int], M[Double]) = {
    val AD = (0 until X.rows).map(r => {
      val xi = X.row(r)
      val (a, d) = centroidIndexAndDistanceClosestTo(distance, μ, xi)
      Vector(a, d)
    }).transpose
    // TODO: remove the map(_.toInt)
    (matrix(X.rows, 1, AD(0).map(_.toInt).toArray), matrix(X.rows, 1, AD(1).toArray))
  }

  /**
   * clusterLA
   *
   * @param  scaledX (assumed to be normalized)
   * @param  distance
   * @param  K
   * @param  iterations
   *
   */

  def clusterLA(
    scaledX: M[Double],
    distance: DistanceFunction,
    K: Int,
    iterations: Int): Seq[(M[Double], M[Int], M[Double])] = {
    assert(K < scaledX.rows)
    val μ0 = scaledX(util.Random.shuffle(0 until scaledX.rows).take(K), 0 until scaledX.columns)
    val a0 = zeros[Int](scaledX.rows, 1)
    val d0 = zeros[Double](scaledX.rows, 1)
    (0 until iterations).scanLeft((μ0, a0, d0))((μad: (M[Double], M[Int], M[Double]), i: Int) => {
      val (a, d) = assignmentsAndDistances(distance, scaledX, μad._1)
      val (μ, unassignedClusterIds) = centroids(scaledX, K, a)
      // val replacements = scaledX(util.Random.shuffle(0 until scaledX.rows).take(unassignedClusterIds.length), 0 until scaledX.columns)
      (μ, a, d)
    }).tail
  }

  /**
   * centroids
   *
   * @param X            M x N feature matrix
   * @param K            number of centroids
   * @param assignments: M x 1 column vector of Ints which e
   *
   */

  def centroids(X: M[Double], K: Int, assignments: M[Int]): (M[Double], Seq[Int]) = {
    val A = matrix(X.rows, K, (r: Int, c: Int) => if (c == assignments(r, 0)) 1.0 else 0.0)
    val distances = A.t ⨯ X // K x N
    val counts = A.columnSums.t // K x 1
    val unassignedClusterIds = (0 until K).filter(counts(_, 0) == 0.0)
    (distances.divColumnVector(counts), unassignedClusterIds)
  }

  /**
   * KMeansClassifier[T]
   *
   * @tparam T       type of the objects being classified
   *
   * @param N                number of features
   * @param featureExtractor creates a list of features (Doubles) of length N given a T
   * @param constructor      creates a T from list of arguments of length N
   * @param μ                K x N Matrix[Double], where each row is a centroid
   * @param scaledX          M x N
   * @param A                M x 1
   * @param distanceLog      K x iterations
   */

  case class KMeansClassifier[T](
    N: Int,
    featureExtractor: T => Seq[Double],
    constructor: Seq[Double] => T,
    μ: M[Double],
    normalizer: FeatureNormalizer,
    distance: DistanceFunction,
    A: M[Int],
    countLog: Seq[M[Int]],
    distanceLog: Seq[M[Double]]) {

    def K(): Int = μ.rows

    val exemplars = (0 until K).map(i => constructor(normalizer.denormalize(μ.row(i)))).toList

    def exemplar(i: Int): T = exemplars(i)

    def classify(observation: T): Int = {
      val (i, d) = centroidIndexAndDistanceClosestTo(distance, μ, normalizer.normalize(featureExtractor(observation)))
      i
    }

    def distanceTreeMap(centroidId: Int): SortedMap[Int, Double] = new immutable.TreeMap[Int, Double]() ++
      distanceLog.zipWithIndex.map({ case (dl, i) => i -> dl(centroidId, 0) }).toMap

    def countTreeMap(centroidId: Int): SortedMap[Int, Int] = new immutable.TreeMap[Int, Int]() ++
      countLog.zipWithIndex.map({ case (cl, i) => i -> cl(centroidId, 0) }).toMap

    def averageDistanceTreeMap(centroidId: Int): SortedMap[Int, Double] = new immutable.TreeMap[Int, Double]() ++
      distanceLog.zip(countLog).zipWithIndex.map({ case ((dl, cl), i) => i -> dl(centroidId, 0) / cl(centroidId, 0) }).toMap

    def distanceLogSeries(): Seq[(String, SortedMap[Int, Double])] = (0 until K()).map(i =>
      ("centroid " + i, distanceTreeMap(i))).toList

    def averageDistanceLogSeries(): Seq[(String, SortedMap[Int, Double])] = (0 until K()).map(i =>
      ("centroid " + i, averageDistanceTreeMap(i))).toList

    def confusionMatrix[L](data: Seq[T], labelExtractor: T => L) = new ConfusionMatrix(this, data, labelExtractor)
  }

  class ConfusionMatrix[T, L](classifier: KMeansClassifier[T], data: Seq[T], labelExtractor: T => L) {

    val label2clusterId = data.map(datum => (labelExtractor(datum), classifier.classify(datum)))

    val labelList = label2clusterId.map(_._1).toSet.toList
    val labelIndices = labelList.zipWithIndex.toMap

    val labelIdClusterId2count = label2clusterId
      .map({ case (label, clusterId) => ((labelIndices(label), clusterId), 1) })
      .groupBy(_._1)
      .map({ case (k, v) => (k, v.map(_._2).sum) })
      .withDefaultValue(0)

    val counts = matrix[Int](labelList.length, classifier.K, (r: Int, c: Int) => labelIdClusterId2count((r, c)))

    val formatNumber = (i: Int) => ("%" + 5 + "d").format(i)

    lazy val rowSums = counts.rowSums()
    lazy val columnSums = counts.columnSums()

    lazy val asString = (labelList.zipWithIndex.map({
      case (label, r) => ((0 until counts.columns).map(c => formatNumber(counts(r, c))).mkString(" ") + " : " + formatNumber(rowSums(r, 0)) + " " + label + "\n")
    }).mkString("")) + "\n" +
      (0 until counts.columns).map(c => formatNumber(columnSums(0, c))).mkString(" ") + "\n"

    override def toString() = asString
  }

}
