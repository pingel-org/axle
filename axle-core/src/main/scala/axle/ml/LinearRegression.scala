package axle.ml

import scala.collection.immutable.TreeMap
import axle.matrix.MatrixModule


trait LinearRegressionModule extends MatrixModule with FeatureNormalizerModule {

  def normalEquation(X: Matrix[Double], y: Matrix[Double]) = (X.t ⨯ X).inv ⨯ X.t ⨯ y

  def h(xi: Matrix[Double], θ: Matrix[Double]) = xi ⨯ θ

  def cost(xi: Matrix[Double], θ: Matrix[Double], yi: Double) = h(xi, θ) - yi

  def dθ(X: Matrix[Double], y: Matrix[Double], θ: Matrix[Double]) = (0 until X.rows)
    .foldLeft(zeros[Double](1, X.columns))(
      (m: Matrix[Double], i: Int) => m + (X.row(i) ⨯ (h(X.row(i), θ) - y(i, 0)))
    ) / X.rows

  def dTheta(X: Matrix[Double], y: Matrix[Double], θ: Matrix[Double]) = dθ(X, y, θ)

  def gradientDescent(X: Matrix[Double], y: Matrix[Double], θ: Matrix[Double], α: Double, iterations: Int) =
    (0 until iterations).foldLeft((θ, List[Double]()))(
      (θiErrLog: (Matrix[Double], List[Double]), i: Int) => {
        val (θi, errLog) = θiErrLog
        val errMatrix = dθ(X, y, θi)
        val errTotal = (0 until errMatrix.rows).map(errMatrix(_, 0)).reduce(_ + _)
        (θi - (errMatrix * α), errTotal :: errLog)
      }
    )

  def regression[D](
    examples: Seq[D],
    numFeatures: Int,
    featureExtractor: D => List[Double],
    objectiveExtractor: D => Double,
    α: Double = 0.1,
    iterations: Int = 100) = {

    val inputX = matrix(
      examples.length,
      numFeatures,
      examples.flatMap(featureExtractor).toArray).t

    val featureNormalizer = new LinearFeatureNormalizer(inputX)

    val X = ones[Double](inputX.rows, 1) +|+ featureNormalizer.normalizedData

    val y = matrix(examples.length, 1, examples.map(objectiveExtractor).toArray)

    val objectiveNormalizer = new LinearFeatureNormalizer(y)

    val θ0 = ones[Double](X.columns, 1)
    val (θ, errLog) = gradientDescent(X, objectiveNormalizer.normalizedData, θ0, α, iterations)

    LinearEstimator(featureExtractor, featureNormalizer, θ, objectiveNormalizer, errLog.reverse)
  }

  case class LinearEstimator[D](
    featureExtractor: D => List[Double],
    featureNormalizer: FeatureNormalizer,
    θ: Matrix[Double],
    objectiveNormalizer: FeatureNormalizer,
    errLog: List[Double]) {

    def errTree() = new TreeMap[Int, Double]() ++
      (0 until errLog.length).map(j => j -> errLog(j)).toMap

    def estimate(observation: D): Double = {
      val scaledX = ones[Double](1, 1) +|+ featureNormalizer.normalize(featureExtractor(observation))
      objectiveNormalizer.denormalize((scaledX ⨯ θ)).head
    }

  }

}
