package axle.ml

import axle._
import axle.matrix._
import collection._

/**
 * KMeans
 *
 */

object KMeansModule extends KMeansModule

trait KMeansModule extends FeatureNormalizerModule {

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

  def Classifier[T](
    data: Seq[T],
    N: Int,
    featureExtractor: T => Seq[Double],
    constructor: Seq[Double] => T,
    distance: (Matrix[Double], Matrix[Double]) => Double,
    K: Int,
    iterations: Int): KMeansClassifier[T] =
    KMeansClassifier(data, N, featureExtractor, constructor, distance, K, iterations)

  // TODO: default distance = distance.euclidean

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
    data: Seq[T],
    N: Int,
    featureExtractor: T => Seq[Double],
    constructor: Seq[Double] => T,
    distance: (Matrix[Double], Matrix[Double]) => Double,
    K: Int,
    iterations: Int) {

    val features = matrix(N, data.length, data.flatMap(featureExtractor(_)).toArray).t

    val normalizer = new PCAFeatureNormalizer(features, 0.95)
    val X = normalizer.normalizedData()
    val μads = clusterLA(X, distance, K, iterations)

    val (μ, a, d) = μads.last

    val assignmentLog = μads.map(_._2)
    val distanceLog = μads.map(_._3)

    val exemplars = (0 until K).map(i => constructor(normalizer.denormalize(μ.row(i)))).toList

    def exemplar(i: Int): T = exemplars(i)

    def classify(observation: T): Int = {
      val (i, d) = centroidIndexAndDistanceClosestTo(distance, μ, normalizer.normalize(featureExtractor(observation)))
      i
    }

    /**
     * centroidIndexAndDistanceClosestTo
     *
     * @param μ
     * @param x
     */

    def centroidIndexAndDistanceClosestTo(
      distance: (Matrix[Double], Matrix[Double]) => Double,
      μ: Matrix[Double],
      x: Matrix[Double]): (Int, Double) =
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

    def assignmentsAndDistances(
      distance: (Matrix[Double], Matrix[Double]) => Double,
      X: Matrix[Double],
      μ: Matrix[Double]): (Matrix[Int], Matrix[Double]) = {
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
     * @param  X (normalized feature matrix)
     * @param  distance
     * @param  K
     * @param  iterations
     *
     */

    def clusterLA(
      X: Matrix[Double],
      distance: (Matrix[Double], Matrix[Double]) => Double,
      K: Int,
      iterations: Int): Seq[(Matrix[Double], Matrix[Int], Matrix[Double])] = {
      assert(K < X.rows)
      val μ0 = X(util.Random.shuffle(0 until X.rows).take(K), 0 until X.columns)
      val a0 = zeros[Int](X.rows, 1)
      val d0 = zeros[Double](X.rows, 1)
      (0 until iterations).scanLeft((μ0, a0, d0))((μad: (Matrix[Double], Matrix[Int], Matrix[Double]), i: Int) => {
        val (a, d) = assignmentsAndDistances(distance, X, μad._1)
        val (μ, unassignedClusterIds) = centroids(X, K, a)
        // val replacements = scaledX(util.Random.shuffle(0 until scaledX.rows).take(unassignedClusterIds.length), 0 until scaledX.columns)
        (μ, a, d)
      }).tail
    }

    /**
     * centroids
     *
     * @param X            M x N scaled feature matrix
     * @param K            number of centroids
     * @param assignments: M x 1 column vector of Ints which e
     *
     */

    def centroids(X: Matrix[Double], K: Int, assignments: Matrix[Int]): (Matrix[Double], Seq[Int]) = {
      val A = matrix(X.rows, K, (r: Int, c: Int) => if (c == assignments(r, 0)) 1.0 else 0.0)
      val distances = A.t ⨯ X // K x N
      val counts = A.columnSums().t // K x 1
      val unassignedClusterIds = (0 until K).filter(counts(_, 0) == 0.0)
      (distances.divColumnVector(counts), unassignedClusterIds)
    }

    def distanceTreeMap(centroidId: Int): SortedMap[Int, Double] = new immutable.TreeMap[Int, Double]() ++
      distanceLog.zipWithIndex.map({ case (dl, i) => i -> dl(centroidId, 0) }).toMap

    //    def countTreeMap(centroidId: Int): SortedMap[Int, Int] = new immutable.TreeMap[Int, Int]() ++
    //      countLog.zipWithIndex.map({ case (cl, i) => i -> cl(centroidId, 0) }).toMap
    //
    //    def averageDistanceTreeMap(centroidId: Int): SortedMap[Int, Double] = new immutable.TreeMap[Int, Double]() ++
    //      distanceLog.zip(countLog).zipWithIndex.map({ case ((dl, cl), i) => i -> dl(centroidId, 0) / cl(centroidId, 0) }).toMap

    def distanceLogSeries(): Seq[(String, SortedMap[Int, Double])] = (0 until K).map(i =>
      ("centroid " + i, distanceTreeMap(i))).toList

    //    def averageDistanceLogSeries(): Seq[(String, SortedMap[Int, Double])] = (0 until K).map(i =>
    //      ("centroid " + i, averageDistanceTreeMap(i))).toList

    def confusionMatrix[L](data: Seq[T], labelExtractor: T => L) = new ConfusionMatrix(this, data, labelExtractor)
  }

  class ConfusionMatrix[T, L](classifier: KMeansClassifier[T], data: Seq[T], labelExtractor: T => L) {

    import math.{ ceil, log10 }

    val label2clusterId = data.map(datum => (labelExtractor(datum), classifier.classify(datum)))

    val labelList = label2clusterId.map(_._1).toSet.toList
    val labelIndices = labelList.zipWithIndex.toMap

    val labelIdClusterId2count = label2clusterId
      .map({ case (label, clusterId) => ((labelIndices(label), clusterId), 1) })
      .groupBy(_._1)
      .map({ case (k, v) => (k, v.map(_._2).sum) })
      .withDefaultValue(0)

    val counts = matrix[Int](labelList.length, classifier.K, (r: Int, c: Int) => labelIdClusterId2count((r, c)))

    val width = ceil(log10(data.length)).toInt

    val formatNumber = (i: Int) => ("%" + width + "d").format(i)

    lazy val rowSums = counts.rowSums()
    lazy val columnSums = counts.columnSums()

    lazy val asString = (labelList.zipWithIndex.map({
      case (label, r) => ((0 until counts.columns).map(c => formatNumber(counts(r, c))).mkString(" ") + " : " + formatNumber(rowSums(r, 0)) + " " + label + "\n")
    }).mkString("")) + "\n" +
      (0 until counts.columns).map(c => formatNumber(columnSums(0, c))).mkString(" ") + "\n"

    override def toString() = asString
  }

}
