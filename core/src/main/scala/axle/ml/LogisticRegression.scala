package axle.ml

object LogisticRegression extends LogisticRegression()

trait LogisticRegression {

  import FeatureNormalizer._
  import axle.matrix.JblasMatrixFactory._
  import math.{ exp, log }

  type M[T] = JblasMatrix[T] // TODO: generalize

  // h is essentially P(y=1 | X;θ)
  def h(xi: M[Double], θ: M[Double]) = 1 / (1 + exp(-1 * (θ.t ⨯ xi).scalar))

  def cost(xi: M[Double], θ: M[Double], yi: Boolean) = -1 * log(yi match {
    case true => h(θ, xi)
    case false => 1 - h(θ, xi)
  })

  def predictedY(xi: M[Double], θ: M[Double]) = h(xi, θ) >= 0.5

  def Jθ(X: M[Double], θ: M[Double], y: M[Boolean]) = (0 until X.rows)
    .foldLeft(0.0)((r: Double, i: Int) => r + cost(X.row(i), θ, y(i, 0))) / X.rows

  def dθ(X: M[Double], y: M[Boolean], θ: M[Double]) = {
    val result = zeros[Double](θ.rows, 1)
    (0 until θ.rows).map(j => result(j, 0) = (0 until X.rows).foldLeft(0.0)(
      (r: Double, i: Int) => {
        val yi = y(i, 0) match { case true => 1.0 case false => 0.0 }
        r + (h(X.row(i), θ) - yi) * X(i, j)
      }))
    result
  }

  // objective: minimize (over θ) the value of Jθ

  def gradientDescent(X: M[Double], y: M[Boolean], θ: M[Double], α: Double, iterations: Int) =
    (0 until iterations).foldLeft(θ)((θi: M[Double], i: Int) => θi - (dθ(X, y, θi) * α))

  def regression[D](
    examples: List[D],
    numObservations: Int,
    observationExtractor: D => List[Double],
    objectiveExtractor: D => Boolean,
    α: Double = 0.1,
    numIterations: Int = 100) = {

    val inputX = matrix(examples.length, numObservations, examples.flatMap(observationExtractor(_)).toArray).t
    val y = matrix(examples.length, 1, examples.map(objectiveExtractor(_)).toArray)
    val normalizer = new LinearFeatureNormalizer(inputX)
    val X = ones[Double](examples.length, 1) +|+ normalizer.normalizedData()
    val θ0 = ones[Double](X.columns, 1)
    val θ = gradientDescent(X, y, θ0, α, numIterations)
    (θ, normalizer)
  }

}
