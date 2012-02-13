package org.pingel.axle.matrix

object LogisticRegression extends LogisticRegression()

class LogisticRegression {

  import org.pingel.axle.matrix.DoubleJblasMatrixFactory._
  import Math.log
  import Math.exp

  def h(xi: Matrix, θ: Matrix) = 1 / (1 + exp(-1 * (θ.t ⨯ xi).scalar))

  // h is essentially P(y=1 | X;θ)

  def cost(xi: Matrix, θ: Matrix, yi: Boolean) = -1 * log(yi match {
    case true => h(θ, xi)
    case false => 1 - h(θ, xi)
  })

  def predictedY(xi: Matrix, θ: Matrix) = h(xi, θ) >= 0.5
/*
  // TODO: y is Boolean
  def Jθ(X: Matrix, θ: Matrix, y: Matrix) =
    (0 until X.rows).foldLeft(0.0)((r: Double, i: Int) => {
      val xi = X.getRow(i)
      val yi = y.getRow(i)
      r + cost(xi, θ, yi.scalar)
    }) / X.rows

  def dθ(X: Matrix, y: Matrix, θ: Matrix) = {
    var result = zeros(θ.rows, 1)
    (0 until θ.rows).map(j =>
      result.setValueAt(j, 0,
        (0 until X.rows).foldLeft(0.0)(
          (r: Double, i: Int) => {
            val xi = X.getRow(i)
            val xij = xi.getColumn(j).scalar
            val yi: Boolean = y.getRow(i).scalar
            r + (h(xi, θ) - yi) * xij
          })))
    result
  }

  // objective: minimize (over θ) the value of Jθ

  def gradientDescent(X: Matrix, y: Matrix, θ: Matrix, α: Double, iterations: Int) =
    (0 until iterations).foldLeft(θ)((θi: Matrix, i: Int) => θi - (dθ(X, y, θi) * α))
*/

}