package axle.ml

object LinearRegression extends LinearRegression()

trait LinearRegression extends Regression {

  import axle.matrix.JblasMatrixFactory._ // TODO: generalize

  def normalEquation(X: M[Double], y: M[Double]) = (X.t ⨯ X).inv ⨯ X.t ⨯ y

  def h(xi: M[Double], θ: M[Double]) = xi ⨯ θ

  def cost(xi: M[Double], θ: M[Double], yi: Double) = h(xi, θ) - yi

  def dθ(X: M[Double], y: M[Double], θ: M[Double]) = (0 until X.rows)
    .foldLeft(zeros[Double](1, X.columns))(
      (m: M[Double], i: Int) => m + (X.row(i) ⨯ (h(X.row(i), θ) - y.valueAt(i, 0)))
    ) / X.rows

  def gradientDescentImmutable(X: M[Double], y: M[Double], θ: M[Double], α: Double, iterations: Int) =
    (0 until iterations).foldLeft(θ)((θi: M[Double], i: Int) => θi - (dθ(X, y, θi) * α))

  def gradientDescentMutable(X: M[Double], y: M[Double], θo: M[Double], α: Double, iterations: Int) = {
    var θi = θo.dup
    var i = 0
    while (i < iterations) {
      θi -= (dθ(X, y, θi) * α)
      i = i + 1
    }
    θi
  }

  def gradientDescent(X: M[Double], y: M[Double], θ: M[Double], α: Double, iterations: Int) = 
    gradientDescentImmutable(X, y, θ, α, iterations)

  // non-unicode alias
  def dTheta(X: M[Double], y: M[Double], θ: M[Double]) = dθ(X, y, θ)

  
  def regression[D](examples: List[D], numObservations: Int, observationExtractor: D => List[Double], objectiveExtractor: D => Double) = {

    val y = matrix(examples.length, 1, examples.map(objectiveExtractor(_)).toArray)

    val inputX = matrix(
      examples.length,
      numObservations,
      examples.flatMap(observationExtractor(_)).toArray).t

    val scaledX = scaleColumns(inputX)

    val X = ones[Double](inputX.rows, 1) +|+ scaledX._1

    val yScaled = scaleColumns(y)
    val θ0 = ones[Double](X.columns, 1)
    val α = 0.1
    val N = 100 // iterations

    val θ = gradientDescent(X, yScaled._1, θ0, α, N)

    θ // TODO also return enough information to scale the result
  }
  
}
