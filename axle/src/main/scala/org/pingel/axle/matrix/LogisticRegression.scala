package org.pingel.axle.matrix

object LogisticRegression extends LogisticRegression()

class LogisticRegression {

  import org.pingel.axle.matrix.JblasMatrixFactory._
  import Math.log
  import Math.exp

  // h is essentially P(y=1 | X;θ)
  def h(xi: JblasMatrix[Double], θ: JblasMatrix[Double]): Double = 1 / (1 + exp(-1 * (θ.t ⨯ xi).scalar))

  def cost(xi: JblasMatrix[Double], θ: JblasMatrix[Double], yi: Boolean) = -1 * log(yi match {
    case true => h(θ, xi)
    case false => 1 - h(θ, xi)
  })

  def predictedY(xi: JblasMatrix[Double], θ: JblasMatrix[Double]) = h(xi, θ) >= 0.5

  def Jθ(X: JblasMatrix[Double], θ: JblasMatrix[Double], y: JblasMatrix[Boolean]) =
    (0 until X.rows).foldLeft(0.0)((r: Double, i: Int) => {
      r + cost(X.getRow(i), θ, y.getRow(i).scalar)
    }) / X.rows

  def dθ(X: JblasMatrix[Double], y: JblasMatrix[Boolean], θ: JblasMatrix[Double]): JblasMatrix[Double] = {
    var result = zeros[Double](θ.rows, 1)
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

  def gradientDescent(X: JblasMatrix[Double], y: JblasMatrix[Boolean], θ: JblasMatrix[Double], α: Double, iterations: Int): JblasMatrix[Double] =
    (0 until iterations).foldLeft(θ)((θi: JblasMatrix[Double], i: Int) => θi - (dθ(X, y, θi) * α))

}