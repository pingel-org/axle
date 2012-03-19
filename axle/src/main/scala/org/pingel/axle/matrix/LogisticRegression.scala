package org.pingel.axle.matrix

object LogisticRegression extends LogisticRegression()

class LogisticRegression {

  import org.pingel.axle.matrix.DoubleJblasMatrixFactory._
  import Math.log
  import Math.exp

  def h(xi: Matrix[Double], θ: Matrix[Double]): Double = 1 / (1 + exp(-1 * (θ.t ⨯ xi).scalar))

  // h is essentially P(y=1 | X;θ)

  def cost(xi: Matrix[Double], θ: Matrix[Double], yi: Boolean) = -1 * log(yi match {
    case true => h(θ, xi)
    case false => 1 - h(θ, xi)
  })

  def predictedY(xi: Matrix[Double], θ: Matrix[Double]) = h(xi, θ) >= 0.5

  def Jθ(X: Matrix[Double], θ: Matrix[Double], y: Matrix[Boolean]) =
    (0 until X.rows).foldLeft(0.0)((r: Double, i: Int) => {
      r + cost(X.getRow(i), θ, y.getRow(i).scalar)
    }) / X.rows

  def dθ(X: Matrix[Double], y: Matrix[Boolean], θ: Matrix[Double]) = {
    var result = zeros(θ.rows, 1)
    (0 until θ.rows).map(j =>
      result.setValueAt(j, 0,
        (0 until X.rows).foldLeft(0.0)(
          (r: Double, i: Int) => {
            val xi = X.getRow(i)
            val xij = xi.getColumn(j).scalar
            val yi: Double = y.getRow(i).scalar match { case true => 1.0 case false => 0.0 }
            r + (h(xi, θ) - yi) * xij
          })))
    result
  }

  // objective: minimize (over θ) the value of Jθ

  def gradientDescent(X: Matrix[Double], y: Matrix[Boolean], θ: Matrix[Double], α: Double, iterations: Int) =
    (0 until iterations).foldLeft(θ)((θi: Matrix[Double], i: Int) => θi - (dθ(X, y, θi) * α))

}