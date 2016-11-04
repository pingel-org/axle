package axle.ml

import spire.math.exp
import spire.math.log

import axle.syntax.linearalgebra._
import axle.algebra.LinearAlgebra
import spire.implicits._

case class LogisticRegression[D, M](
  examples: List[D],
  numFeatures: Int,
  featureExtractor: D => List[Double],
  objectiveExtractor: D => Boolean,
  α: Double = 0.1,
  numIterations: Int = 100)(implicit la: LinearAlgebra[M, Int, Int, Double])
    extends Function1[List[Double], Double] {

  implicit val module = la.module
  implicit val ring = la.ring

  // h is essentially P(y=1 | X;θ)
  def h(xi: M, θ: M): Double =
    1 / (1 + exp(-1 * (θ.t * xi).scalar))

  // yi is boolean (1d or 0d)
  def cost(xi: M, θ: M, yi: Double): Double =
    -1 * log(if (yi > 0d) h(θ, xi) else 1 - h(θ, xi))

  def Jθ(X: M, θ: M, y: M): Double =
    (0 until X.rows)
      .foldLeft(0d)((r: Double, i: Int) => r + cost(X.row(i), θ, y.get(i, 0))) / X.rows

  def dθ(X: M, y: M, θ: M): M = {
    la.matrix(θ.rows, 1, (r: Int, c: Int) => {
      (0 until X.rows).map(i => (h(X.row(i), θ) - y.get(i, 0)) * X.get(i, r)).sum
    })
  }

  /**
   * 
   * Gradient descent's objective: minimize (over θ) the value of Jθ
   */

  def gradientDescent(X: M, y: M, α: Double, iterations: Int): M = {
    val θ0 = la.ones(X.columns, 1)
    (0 until iterations).foldLeft(θ0)((θi: M, i: Int) => la.ring.minus(θi, (dθ(X, y, θi) :* α)))
  }

  val inputX: M = la.fromColumnMajorArray(examples.length, numFeatures, examples.flatMap(featureExtractor).toArray)

  def boolean2double(b: Boolean): Double = b match {
    case true  => 1d
    case false => 0d
  }

  val y = la.fromColumnMajorArray(
    examples.length,
    1,
    examples.map(objectiveExtractor).map(boolean2double).toArray)

  val featureNormalizer = LinearFeatureNormalizer(inputX)

  // val X = la.ones(examples.length, 1) aside featureNormalizer.normalizedData
  val X = featureNormalizer.normalizedData

  val finalθ = gradientDescent(X, y, α, numIterations)

  def apply(features: List[Double]): Double =
    h(featureNormalizer(features), finalθ)

}
