package axle.ml

object LogisticRegression extends LogisticRegression()

trait LogisticRegression {

  import axle.matrix.JblasMatrixFactory._
  type M[T] = JblasMatrix[T] // TODO: generalize
  import Math.{ exp, log }

  // h is essentially P(y=1 | X;θ)
  def h(xi: M[Double], θ: M[Double]) = 1 / (1 + exp(-1 * (θ.t ⨯ xi).scalar))

  def cost(xi: M[Double], θ: M[Double], yi: Boolean) = -1 * log(yi match {
    case true => h(θ, xi)
    case false => 1 - h(θ, xi)
  })

  def predictedY(xi: M[Double], θ: M[Double]) = h(xi, θ) >= 0.5

  def Jθ(X: M[Double], θ: M[Double], y: M[Boolean]) = (0 until X.rows)
    .foldLeft(0.0)((r: Double, i: Int) => {
      r + cost(X.getRow(i), θ, y.getRow(i).scalar)
    }) / X.rows

  def dθ(X: M[Double], y: M[Boolean], θ: M[Double]) = {
    val result = zeros[Double](θ.rows, 1)
    (0 until θ.rows).map(j =>
      result.setValueAt(j, 0,
        (0 until X.rows).foldLeft(0.0)(
          (r: Double, i: Int) => {
            val xi = X.getRow(i)
            val xij = xi.getColumn(j).scalar
            val yi = y.getRow(i).scalar match { case true => 1.0 case false => 0.0 }
            r + (h(xi, θ) - yi) * xij
          })))
    result
  }

  // objective: minimize (over θ) the value of Jθ

  def gradientDescent(X: M[Double], y: M[Boolean], θ: M[Double], α: Double, iterations: Int) =
    (0 until iterations).foldLeft(θ)((θi: M[Double], i: Int) => θi - (dθ(X, y, θi) * α))

}
