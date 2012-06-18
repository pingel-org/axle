package axle.ml

import collection._

object KMeans extends KMeans()

/**
 * KMeans
 * 
 */

trait KMeans {

  import axle.matrix.JblasMatrixFactory._ // TODO: generalize
  type M[T] = JblasMatrix[T]

  def square(x: Double) = x * x

  def √(x: Double) = math.sqrt(x)

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

  def cluster[T](data: Seq[T], N: Int,
    featureExtractor: T => List[Double], constructor: List[Double] => T,
    K: Int, iterations: Int): KMeansClassifier[T] = {

    val X = matrix(N, data.length, data.flatMap(featureExtractor(_)).toArray).t
    val distanceLog = zeros[Double](K, iterations)

    val (scaledX, colMins, colRanges) = Utilities.scaleColumns(X)
    val (μ, c) = clusterLA(scaledX, K, iterations, distanceLog)

    KMeansClassifier(N, featureExtractor, constructor, μ, colMins, colRanges, scaledX, c, distanceLog)
  }

  /**
   * distanceRow
   *
   * @param r1
   * @param r2
   *
   */

  def distanceRow(r1: M[Double], r2: M[Double]): Double = {
    // assert(r1.isRowVector && r2.isRowVector && r1.length == r2.length)
    val dRow = r1 - r2
    math.sqrt((0 until r1.columns).map(i => square(dRow(0, i))).reduce(_ + _))
  }

  /**
   * centroidIndexAndDistanceClosestTo
   *
   * @param μ
   * @param x
   */

  def centroidIndexAndDistanceClosestTo(μ: M[Double], x: M[Double]): (Int, Double) =
    (0 until μ.rows)
      .map(r => (r, distanceRow(μ.row(r), x)))
      .minBy(_._2)

  /**
   * assignments
   *
   * @param X
   * @param μ
   * @param distanceLog
   * @param i
   *
   * Returns:
   * N x 1 matrix: indexes of centroids closest to xi
   *
   */

  def assignments(X: M[Double], μ: M[Double], distanceLog: M[Double], i: Int): M[Int] = {
    val A = zeros[Int](X.rows, 1)
    for (r <- 0 until X.rows) {
      val ad = centroidIndexAndDistanceClosestTo(μ, X.row(r))
      A(r, 0) = ad._1
      distanceLog(ad._1, i) += ad._2
    }
    A
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
   * clusterLA
   *
   * @param  scaledX
   * @param  K
   * @param  iterations
   * @param  distanceLog
   *
   * assumes that X has already been normalized
   */

  def clusterLA(scaledX: M[Double], K: Int, iterations: Int, distanceLog: M[Double]): (M[Double], M[Int]) = {
    assert(K < scaledX.rows)
    (0 until iterations).foldLeft((
      rand[Double](K, scaledX.columns), // random initial K centroids μ in R^n (aka M)
      zeros[Int](scaledX.rows, 1)) // indexes of centroids closest to xi
    )((μA: (M[Double], M[Int]), i: Int) => {
      val A = assignments(scaledX, μA._1, distanceLog, i) // K-element column vector
      val μ = centroids(scaledX, K, A) // K x n
      (μ, A)
    })
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
   * @param colMins          1 x N
   * @param colRanges        1 x N
   * @param scaledX          ? x N
   * @param A                ? x 1
   * @param distanceLog      K x iterations
   */

  case class KMeansClassifier[T](
    N: Int,
    featureExtractor: T => List[Double],
    constructor: List[Double] => T,
    μ: M[Double],
    colMins: M[Double],
    colRanges: M[Double],
    scaledX: M[Double],
    A: M[Int],
    distanceLog: M[Double]) {

    def K(): Int = μ.rows

    val exemplars = (0 until K).map(i => constructor(((μ.row(i) ⨯ diag(colRanges)) + colMins).toList)).toList

    def exemplar(i: Int): T = exemplars(i)

    def classify(observation: T): Int = {
      val featureList = featureExtractor(observation)
      val featureRowMatrix = matrix(1, featureList.length, featureList.toArray)
      val scaledX = diag(colRanges).inv ⨯ (featureRowMatrix.subRowVector(colMins).t)
      val cid = centroidIndexAndDistanceClosestTo(μ, scaledX)
      cid._1
    }

    def distanceLogTreeMaps() = (0 until K()).map(i =>
      new immutable.TreeMap[Int, Double]() ++ (0 until distanceLog.columns).map(j => j -> distanceLog(i, j)).toMap
    )

    // def draw(): Unit = new KMeansVisualization(this).draw()
  }

}
