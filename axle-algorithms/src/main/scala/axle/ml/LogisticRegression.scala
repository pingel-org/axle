package axle.ml

import scala.math.exp
import scala.math.log

import axle.syntax.linearalgebra._
import axle.algebra.LinearAlgebra
import spire.implicits._

case class LogisticRegression[D, M](
  examples: List[D],
  numObservations: Int,
  observationExtractor: D => List[Double],
  objectiveExtractor: D => Boolean,
  α: Double = 0.1,
  numIterations: Int = 100)(implicit la: LinearAlgebra[M, Double]) {

  implicit val module = la.module
  implicit val ring = la.ring

  // h is essentially P(y=1 | X;θ)
  def h(xi: M, θ: M): Double = 1 / (1 + exp(-1 * (θ.t * xi).scalar))

  // yi is boolean (1d or 0d)
  def cost(xi: M, θ: M, yi: Double) =
    -1 * log(if (yi > 0d) h(θ, xi) else 1 - h(θ, xi))

  def predictedY(xi: M, θ: M): Boolean =
    h(xi, θ) >= 0.5

  def Jθ(X: M, θ: M, y: M) =
    (0 until X.rows)
      .foldLeft(0d)((r: Double, i: Int) => r + cost(X.row(i), θ, y.get(i, 0))) / X.rows

  def dθ(X: M, y: M, θ: M): M = {
    la.matrix(θ.rows, 1, (r: Int, c: Int) => {
      (0 until X.rows).map(i => (h(X.row(i), θ) - y.get(i, 0)) * X.get(i, r)).sum
    })
  }

  // objective: minimize (over θ) the value of Jθ

  def gradientDescent(X: M, y: M, θ: M, α: Double, iterations: Int): M =
    (0 until iterations).foldLeft(θ)((θi: M, i: Int) => la.ring.minus(θi, (dθ(X, y, θi) :* α)))

  val inputX = la.matrix(examples.length, numObservations, examples.flatMap(observationExtractor).toArray).t

  val y = la.matrix(
    examples.length,
    1,
    examples.map(objectiveExtractor).map(o => o match { case true => 1d case false => 0d }).toArray)

  val featureNormalizer = LinearFeatureNormalizer(inputX)

  val X = la.ones(examples.length, 1) +|+ featureNormalizer.normalizedData

  val θ0 = la.ones(X.columns, 1)

  val θ = gradientDescent(X, y, θ0, α, numIterations)

  // TODO: These were returned by 'regression' method previously:
  (θ, featureNormalizer)

}
