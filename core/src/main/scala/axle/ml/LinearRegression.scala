package axle.ml

import collection._

object LinearRegression extends LinearRegression()

trait LinearRegression {

  import axle.matrix.JblasMatrixFactory._ // TODO: generalize
  type M[T] = JblasMatrix[T] // TODO: generalize

  import Utilities._

  def normalEquation(X: M[Double], y: M[Double]) = (X.t ⨯ X).inv ⨯ X.t ⨯ y

  def h(xi: M[Double], θ: M[Double]) = xi ⨯ θ

  def cost(xi: M[Double], θ: M[Double], yi: Double) = h(xi, θ) - yi

  def dθ(X: M[Double], y: M[Double], θ: M[Double]) = (0 until X.rows)
    .foldLeft(zeros[Double](1, X.columns))(
      (m: M[Double], i: Int) => m + (X.row(i) ⨯ (h(X.row(i), θ) - y(i, 0)))
    ) / X.rows

  def dTheta(X: M[Double], y: M[Double], θ: M[Double]) = dθ(X, y, θ)

  def gradientDescent(X: M[Double], y: M[Double], θ: M[Double], α: Double, iterations: Int) =
    (0 until iterations).foldLeft((θ, List[Double]()))(
      (θiErrLog: (M[Double], List[Double]), i: Int) => {
        val (θi, errLog) = θiErrLog
        val errMatrix = dθ(X, y, θi)
        val errTotal = (0 until errMatrix.rows).map(errMatrix(_, 0)).reduce(_ + _)
        (θi - (errMatrix * α), errTotal :: errLog)
      }
    )

  def regression[D](
    examples: Seq[D],
    numObservations: Int,
    featureExtractor: D => List[Double],
    objectiveExtractor: D => Double,
    α: Double = 0.1,
    N: Int = 100 // iterations
    ) = {

    val inputX = matrix(
      examples.length,
      numObservations,
      examples.flatMap(featureExtractor(_)).toArray).t

    val (scaledX, colMins, colRanges) = scaleColumns(inputX)
    val X = ones[Double](inputX.rows, 1) +|+ scaledX

    val y = matrix(examples.length, 1, examples.map(objectiveExtractor(_)).toArray)
    val (scaledY, yMin, yRange) = scaleColumns(y)
    val θ0 = ones[Double](X.columns, 1)

    val (θ, errLog) = gradientDescent(X, scaledY, θ0, α, N)

    LinearEstimator(featureExtractor, colMins, colRanges, θ, yMin, yRange, errLog.reverse)
  }

  case class LinearEstimator[D](
    featureExtractor: D => List[Double],
    colMins: M[Double],
    colRanges: M[Double],
    θ: M[Double],
    yMin: M[Double],
    yRange: M[Double],
    errLog: List[Double]) {

    def errTree() = new immutable.TreeMap[Int, Double]() ++
      (0 until errLog.length).map(j => j -> errLog(j)).toMap

    def estimate(observation: D) = {
      val featureList = featureExtractor(observation)
      val featureRowMatrix = matrix(1, featureList.length, featureList.toArray)
      val scaledX = ones[Double](1, 1) +|+ (diag(colRanges).inv ⨯ (featureRowMatrix.subRowVector(colMins).t)).t
      val scaledY = (scaledX ⨯ θ).scalar
      (scaledY * yRange.scalar) + yMin.scalar
    }

  }

}
