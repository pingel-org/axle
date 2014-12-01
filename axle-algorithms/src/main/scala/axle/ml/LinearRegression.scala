package axle.ml

import scala.collection.immutable.TreeMap
import axle.algebra.Matrix
import axle.syntax.matrix._

case class LinearRegression[D, M[_]](
  examples: Seq[D],
  numFeatures: Int,
  featureExtractor: D => List[Double],
  objectiveExtractor: D => Double,
  α: Double = 0.1,
  iterations: Int = 100)(implicit ev: Matrix[M]) {

  val inputX = ev.matrix(
    examples.length,
    numFeatures,
    examples.flatMap(featureExtractor).toArray).t

  val featureNormalizer = LinearFeatureNormalizer(inputX)

  val X = ev.ones[Double](inputX.rows, 1) +|+ featureNormalizer.normalizedData

  val y = ev.matrix(examples.length, 1, examples.map(objectiveExtractor).toArray)

  val objectiveNormalizer = LinearFeatureNormalizer(y)

  val θ0 = ev.ones[Double](X.columns, 1)
  val (θ, errLog) = gradientDescent(X, objectiveNormalizer.normalizedData, θ0, α, iterations)

  def normalEquation(X: M[Double], y: M[Double]) = (X.t ⨯ X).inv ⨯ X.t ⨯ y

  def h(xi: M[Double], θ: M[Double]): M[Double] = xi ⨯ θ

  def cost(xi: M[Double], θ: M[Double], yi: Double): Double = h(xi, θ).scalar - yi

  def dθ(X: M[Double], y: M[Double], θ: M[Double]): M[Double] =
    (0 until X.rows)
      .foldLeft(ev.zeros[Double](1, X.columns))(
        (m: M[Double], i: Int) => m + (X.row(i) ⨯ (h(X.row(i), θ).subtractScalar(y.get(i, 0))))) / X.rows

  def dTheta(X: M[Double], y: M[Double], θ: M[Double]): M[Double] = dθ(X, y, θ)

  def gradientDescent(X: M[Double], y: M[Double], θ: M[Double], α: Double, iterations: Int) =
    (0 until iterations).foldLeft((θ, List[Double]()))(
      (θiErrLog: (M[Double], List[Double]), i: Int) => {
        val (θi, errLog) = θiErrLog
        val errMatrix = dθ(X, y, θi)
        val errTotal = (0 until errMatrix.rows).map(errMatrix.get(_, 0)).sum
        (θi - (errMatrix * α), errTotal :: errLog)
      })

  def errTree = new TreeMap[Int, Double]() ++
    (0 until errLog.reverse.length).map(j => j -> errLog(j)).toMap

  def estimate(observation: D): Double = {
    val scaledX = ev.ones[Double](1, 1) +|+ featureNormalizer(featureExtractor(observation))
    objectiveNormalizer.unapply((scaledX ⨯ θ)).head
  }
}
