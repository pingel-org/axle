package axle.ml

import scala.collection.immutable.TreeMap
import spire.math.abs
import spire.algebra.Module
import spire.algebra.Ring
// import spire.implicits.IntAlgebra
import spire.implicits._
import axle.algebra.LinearAlgebra
import axle.syntax.linearalgebra._

case class LinearRegression[D, M](
  examples: Seq[D],
  numFeatures: Int,
  featureExtractor: D => Seq[Double],
  objectiveExtractor: D => Double,
  α: Double = 0.1,
  iterations: Int = 100)(implicit la: LinearAlgebra[M, Int, Int, Double])
    extends Function1[D, Double] {

  implicit val ringM: Ring[M] = la.ring
  implicit val module: Module[M, Double] = la.module
  // implicit val zeroInt = axle.algebra.Zero.ringZero[Int]

  val inputX = la.fromRowMajorArray(
    examples.length,
    numFeatures,
    examples.flatMap(featureExtractor).toArray)

  val featureNormalizer = LinearFeatureNormalizer(inputX)

  val X = la.ones(inputX.rows, 1) +|+ featureNormalizer.normalizedData

  val y = la.fromColumnMajorArray(examples.length, 1, examples.map(objectiveExtractor).toArray)

  val objectiveNormalizer = LinearFeatureNormalizer(y)

  val θ0 = la.ones(X.columns, 1)

  val (θ, errorLogReversed) = gradientDescent(X, objectiveNormalizer.normalizedData, θ0, α, iterations)

  def normalEquation(X: M, y: M) = (X.t * X).inv * X.t * y

  def h(xi: M, θ: M): M = xi * θ

  def cost(xi: M, θ: M, yi: Double): Double = h(xi, θ).scalar - yi

  def dθ(X: M, y: M, θ: M): M =
    (0 until X.rows)
      .foldLeft(la.zeros(1, X.columns))(
        (m: M, i: Int) => ringM.plus(m, (X.row(i) * (h(X.row(i), θ).subtractScalar(y.get(i, 0)))))).divideScalar(X.rows)

  def dTheta(X: M, y: M, θ: M): M = dθ(X, y, θ)

  def gradientDescent(X: M, y: M, θ: M, α: Double, iterations: Int) =
    (0 until iterations).foldLeft((θ, List.empty[Double]))(
      (θiErrLog: (M, List[Double]), i: Int) => {
        val (θi, errLog) = θiErrLog
        val errMatrix = dθ(X, y, θi)
        val error = (0 until errMatrix.rows).map(i => abs(errMatrix.get(i, 0))).sum
        (la.ring.minus(θi, (errMatrix :* α)), error :: errLog)
      })

  def errTree = {
    val errorLog = errorLogReversed.reverse.toVector
    new TreeMap[Int, Double]() ++
      (0 until errorLog.length).map(j => j -> errorLog(j)).toMap
  }

  def apply(observation: D): Double = {
    val scaledX = la.ones(1, 1) +|+ featureNormalizer(featureExtractor(observation))
    objectiveNormalizer.unapply((scaledX * θ)).head
  }
}
