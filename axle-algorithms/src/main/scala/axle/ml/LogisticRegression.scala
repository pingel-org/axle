package axle.ml

import scala.math.exp
import scala.math.log

import axle.syntax.matrix._
import axle.algebra.Matrix

case class LogisticRegression[D, M[_]: Matrix](
  examples: List[D],
  numObservations: Int,
  observationExtractor: D => List[Double],
  objectiveExtractor: D => Boolean,
  α: Double = 0.1,
  numIterations: Int = 100) {

  val witness = implicitly[Matrix[M]]

  // h is essentially P(y=1 | X;θ)
  def h(xi: M[Double], θ: M[Double]): Double = 1 / (1 + exp(-1 * (θ.t ⨯ xi).scalar))

  def cost(xi: M[Double], θ: M[Double], yi: Boolean) =
    -1 * log(if (yi) h(θ, xi) else 1 - h(θ, xi))

  def predictedY(xi: M[Double], θ: M[Double]): Boolean =
    h(xi, θ) >= 0.5

  def Jθ(X: M[Double], θ: M[Double], y: M[Boolean]) =
    (0 until X.rows)
      .foldLeft(0d)((r: Double, i: Int) => r + cost(X.row(i), θ, y.get(i, 0))) / X.rows

  def dθ(X: M[Double], y: M[Boolean], θ: M[Double]): M[Double] = {
    val yd = y.map(_ match { case true => 1d case false => 0d })
    witness.matrix(θ.rows, 1, (r: Int, c: Int) => {
      (0 until X.rows).map(i => (h(X.row(i), θ) - yd.get(i, 0)) * X.get(i, r)).sum
    })
  }

  // objective: minimize (over θ) the value of Jθ

  def gradientDescent(X: M[Double], y: M[Boolean], θ: M[Double], α: Double, iterations: Int) =
    (0 until iterations).foldLeft(θ)((θi: M[Double], i: Int) => θi - (dθ(X, y, θi) * α))

  val inputX = witness.matrix(examples.length, numObservations, examples.flatMap(observationExtractor).toArray).t

  val y = witness.matrix[Boolean](examples.length, 1, examples.map(objectiveExtractor).toArray)

  val featureNormalizer = LinearFeatureNormalizer(inputX)

  val X = witness.ones[Double](examples.length, 1) +|+ featureNormalizer.normalizedData

  val θ0 = witness.ones[Double](X.columns, 1)

  val θ = gradientDescent(X, y, θ0, α, numIterations)

  // TODO: These were returned by 'regression' method previously:
  (θ, featureNormalizer)

}
