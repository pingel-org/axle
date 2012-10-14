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

    val features = matrix(N, data.length, data.flatMap(featureExtractor(_)).toArray).t
    val distanceLog = zeros[Double](K, iterations)
    val countLog = zeros[Int](K, iterations)

    // val normalizer = new LinearFeatureNormalizer(features)
    // val normalizer = new ZScoreFeatureNormalizer(features)
    val normalizer = new PCAFeatureNormalizer(features, 0.95)

    val nd = normalizer.normalizedData()

    val (μ, c) = clusterLA(nd, distance, K, iterations, distanceLog, countLog)

    KMeansClassifier(N, featureExtractor, constructor, μ, normalizer, distance, c, distanceLog, countLog)
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
   * assignments
   *
   * @param X
   * @param μ
   * @param distanceLog
   * @param countLog
   * @param i
   *
   * Returns:
   * N x 1 matrix: indexes of centroids closest to xi
   *
   */

  def assignments(distance: DistanceFunction, X: M[Double], μ: M[Double], distanceLog: M[Double], countLog: M[Int], i: Int): M[Int] = {
    val A = zeros[Int](X.rows, 1)
    for (r <- 0 until X.rows) {
      val ad = centroidIndexAndDistanceClosestTo(distance, μ, X.row(r))
      A(r, 0) = ad._1
      countLog(ad._1, i) += 1
      distanceLog(ad._1, i) += ad._2
    }
    A
  }

  /**
   * clusterLA
   *
   * @param  scaledX
   * @param  K
   * @param  iterations
   * @param  distanceLog
   *
   * assumes that X has already been normalized
   */

  def clusterLA(scaledX: M[Double], distance: DistanceFunction, K: Int, iterations: Int, distanceLog: M[Double], countLog: M[Int]): (M[Double], M[Int]) = {
    assert(K < scaledX.rows)
    (0 until iterations).foldLeft((
      rand[Double](K, scaledX.columns), // random initial K centroids μ in R^n (aka M)
      zeros[Int](scaledX.rows, 1)) // indexes of centroids closest to xi
    )((μA: (M[Double], M[Int]), i: Int) => {
      val A = assignments(distance, scaledX, μA._1, distanceLog, countLog, i) // K-element column vector
      val μ = centroids(scaledX, K, A) // K x n
      (μ, A)
    })
  }

  /**
   * centroids
   *
   * @param X
   * @param K
   * @param A
   *
   */

  def centroids(X: M[Double], K: Int, A: M[Int]): M[Double] = {
    val accumulator = zeros[Double](K, X.columns)
    val counts = zeros[Int](K, 1) // Note: Could be a M[Int]
    for (r <- 0 until X.rows) {
      val x = X.row(r)
      val a = A(r, 0)
      counts(a, 0) += 1
      for (c <- 0 until X.columns) {
        accumulator(a, c) += x(0, c)
      }
    }

    // accumulator ⨯ counts.inv
    // TODO rephrase this using linear algebra:
    for (r <- 0 until K) {
      val v = counts(r, 0)
      for (c <- 0 until X.columns) {
        if (v == 0) {
          accumulator(r, c) = math.random // TODO verify KMeans algorithm
        } else {
          accumulator(r, c) /= v
        }
      }
    }
    accumulator
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
   * @param scaledX          ? x N
   * @param A                ? x 1
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
    distanceLog: M[Double],
    countLog: M[Int]) {

    def K(): Int = μ.rows

    val exemplars = (0 until K).map(i => constructor(normalizer.denormalize(μ.row(i)))).toList

    def exemplar(i: Int): T = exemplars(i)

    def classify(observation: T): Int = {
      val (i, d) = centroidIndexAndDistanceClosestTo(distance, μ, normalizer.normalize(featureExtractor(observation)))
      i
    }

    def distanceTreeMap(i: Int): SortedMap[Int, Double] = new immutable.TreeMap[Int, Double]() ++
      (0 until distanceLog.columns).map(j => j -> distanceLog(i, j)).toMap

    def countTreeMap(i: Int): SortedMap[Int, Int] = new immutable.TreeMap[Int, Int]() ++
      (0 until countLog.columns).map(j => j -> countLog(i, j)).toMap

    def averageDistanceTreeMap(i: Int): SortedMap[Int, Double] = new immutable.TreeMap[Int, Double]() ++
      (0 until distanceLog.columns).map(j => j -> distanceLog(i, j) / countLog(i, j)).toMap

    def distanceLogSeries(): Seq[(String, SortedMap[Int, Double])] = (0 until K()).map(i =>
      ("centroid " + i, distanceTreeMap(i))).toList

    def averageDistanceLogSeries(): Seq[(String, SortedMap[Int, Double])] = (0 until K()).map(i =>
      ("centroid " + i, averageDistanceTreeMap(i))).toList

    def confusionMatrix[L](data: Seq[T], labelExtractor: T => L) = new ConfusionMatrix(this, data, labelExtractor)
  }

  class ConfusionMatrix[T, L](classifier: KMeansClassifier[T], data: Seq[T], labelExtractor: T => L) {

    val actualAndPredictedLabels = data.map(datum => (labelExtractor(datum), classifier.classify(datum)))

    val labelList = actualAndPredictedLabels.map(_._1).toSet.toList
    val labelIndices = labelList.zipWithIndex.toMap

    val counts = zeros[Int](labelList.length, classifier.K)
    actualAndPredictedLabels.map {
      case (label, predictedClusterIndex) => counts(labelIndices(label), predictedClusterIndex) += 1
    }

    lazy val rowSums = counts.rowSums()

    lazy val asString = (labelList.zipWithIndex.map({
      case (label, r) => (counts.row(r).toString + " : " + rowSums(r, 0) + " " + label + "\n")
    }).mkString("")) + counts.columnSums().toString + "\n"

    override def toString() = asString
  }

}
