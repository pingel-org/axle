package axle.ml

object KMeans extends KMeans()

trait KMeans {

  import axle.matrix.JblasMatrixFactory._ // TODO: generalize
  type M[T] = JblasMatrix[T]

  def square(x: Double) = x * x

  import Math.sqrt
  
  def √(x: Double) = sqrt(x)

  def rowToList[D](row: M[D]) = (0 until row.columns).map(row(0, _)).toList

  /**
   * cluster[T]
   * 
   * @typeparam T  type of the objects being classified
   * 
   * @param data
   * @param N
   * @param featureExtractor
   * @param constructor
   * 
   */
  
  def cluster[T](
    data: Seq[T],
    N: Int,
    featureExtractor: T => List[Double],
    constructor: List[Double] => T,
    numFeatures: Int,
    K: Int,
    iterations: Int): KMeansClassifier[T] = {

    val X = matrix(
      data.length,
      numFeatures,
      data.flatMap(featureExtractor(_)).toArray).t

    val (scaledX, colMins, colRanges) = Utilities.scaleColumns(X)
    val (μ, c) = clusterLA(scaledX, K, iterations)
    KMeansClassifier(N, featureExtractor, constructor, μ, colMins, colRanges)
  }

  def distanceRow(r1: M[Double], r2: M[Double]): Double = {
    // assert(r1.isRowVector && r2.isRowVector && r1.length == r2.length)
    val dRow = r1 - r2
    sqrt((0 until r1.columns).map(i => square(dRow(0, i))).reduce(_ + _))
  }

  def centroidIndexClosestTo(μ: M[Double], x: M[Double]): Int = {
    val distances = (0 until μ.columns).map(k => distanceRow(μ.row(k), x))
    val minVI = distances.zipWithIndex.minBy(_._1)
    minVI._2
  }

  // indexes of centroids closest to xi
  def assignments(X: M[Double], μ: M[Double]): M[Int] = {
    val C = zeros[Int](X.rows, 1)
    for (i <- 0 until X.rows) {
      C(i, 0) = centroidIndexClosestTo(μ, X.row(i))
    }
    C
  }

  def centroids(X: M[Double], K: Int, C: M[Int]): M[Double] = {
    val accumulator = zeros[Double](K, X.columns)
    val counts = zeros[Double](K, 1) // Note: Could be a M[Int]
    for (i <- 0 until X.rows) {
      val xi = X.row(i)
      val a = C(i, 0)
      counts(a, 0) += 1
      for (c <- 0 until X.columns) {
        accumulator(a, c) += xi(0, c)
      }
    }
    diag(counts).inv ⨯ accumulator
  }

  /**
   * assumes that X has already been normalized
   */

  def clusterLA(X: M[Double], K: Int, iterations: Int): (M[Double], M[Int]) = {
    assert(K < X.rows)
    (0 until iterations).foldLeft((
      rand[Double](K, X.columns), // random initial K centroids μ in R^n (aka M)
      zeros[Int](X.rows, 1)) // indexes of centroids closest to xi
    )((μC: (M[Double], M[Int]), i: Int) => {
      val C = assignments(X, μC._1) // K-element column vector
      val μ = centroids(X, K, C) // K x n
      (μ, C)
    })
  }

  /**
   * KMeansClassifier[D]
   *
   * @typeparam D       type of the objects being classified
   *
   * @param N                see definitions of featureExtractor and constructor
   * @param featureExtractor creates a list of features (Doubles) of length N given a D
   * @param constructor      creates a D from list of arguments of length N
   * @param μ                a K x n Matrix[Double], where each row is a centroid
   * @param colMins
   * @param colRanges
   */

  case class KMeansClassifier[D](
    N: Int,
    featureExtractor: D => List[Double],
    constructor: List[Double] => D,
    μ: M[Double],
    colMins: M[Double],
    colRanges: M[Double]) {

    def K(): Int = μ.rows

    def exemplar(i: Int): D = {
      val centroid = μ.row(i)
      val unscaledCentroid = (diag(colRanges) ⨯ centroid) + colMins
      constructor(rowToList(unscaledCentroid))
    }

    def classify(observation: D): Int = {
      val featureList = featureExtractor(observation)
      val featureRowMatrix = matrix(1, featureList.length, featureList.toArray)
      val scaledX = diag(colRanges).inv ⨯ (featureRowMatrix.subRowVector(colMins).t)
      centroidIndexClosestTo(μ, scaledX)
    }

  }

}
