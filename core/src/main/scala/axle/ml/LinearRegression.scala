package axle.ml

import axle.matrix.JblasMatrixFactory._

object LinearRegression extends LinearRegression()

class LinearRegression {

  def normalEquation(X: JblasMatrix[Double], y: JblasMatrix[Double]) = (X.t ⨯ X).inv ⨯ X.t ⨯ y

  def scaleColumns(X: JblasMatrix[Double]) = {
    val colMins = X.columnMins
    val colRanges = X.columnMaxs - colMins
    val scaled = (diag(colRanges).inv ⨯ X.subRowVector(colMins).t).t
    (scaled, colMins, colRanges)
  }

  def h(xi: JblasMatrix[Double], θ: JblasMatrix[Double]) = xi ⨯ θ

  def cost(xi: JblasMatrix[Double], θ: JblasMatrix[Double], yi: Double) = h(xi, θ) - yi

  def dθ(X: JblasMatrix[Double], y: JblasMatrix[Double], θ: JblasMatrix[Double]) =
    (0 until X.rows).foldLeft(zeros[Double](1, X.columns))(
      (m: JblasMatrix[Double], i: Int) => {
        m + (X.getRow(i) ⨯ (h(X.getRow(i), θ) - y.valueAt(i, 0)))
      }
    ) / X.rows

  def gradientDescentImmutable(
    X: JblasMatrix[Double],
    y: JblasMatrix[Double],
    θ: JblasMatrix[Double],
    α: Double,
    iterations: Int) =
    (0 until iterations).foldLeft(θ)((θi: JblasMatrix[Double], i: Int) => θi - (dθ(X, y, θi) * α))

  def gradientDescentMutable(
    X: JblasMatrix[Double],
    y: JblasMatrix[Double],
    θo: JblasMatrix[Double],
    α: Double,
    iterations: Int) = {
    var θi = θo.dup
    var i = 0
    while (i < iterations) {
      θi -= (dθ(X, y, θi) * α)
      i = i + 1
    }
    θi
  }

  // non-unicode alias
  def dTheta(X: JblasMatrix[Double], y: JblasMatrix[Double], θ: JblasMatrix[Double]) = dθ(X, y, θ)

}
