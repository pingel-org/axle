package org.pingel.axle.matrix

object LinearRegression extends LinearRegression()

class LinearRegression {

  import org.pingel.axle.matrix.DoubleJblasMatrixFactory._

  def normalEquation(X: Matrix[Double], y: Matrix[Double]) = (X.t ⨯ X).inv ⨯ X.t ⨯ y

  def scaleColumns(X: Matrix[Double]) = {
    val colMins = X.columnMins
    val colRanges = X.columnMaxs - colMins
    val scaled = (colRanges.diag().inv ⨯ X.subRowVector(colMins).t).t
    (scaled, colMins, colRanges)
  }

  def h(xi: Matrix[Double], θ: Matrix[Double]) = xi ⨯ θ

  def cost(xi: Matrix[Double], θ: Matrix[Double], yi: Double) = h(xi, θ) - yi

  def dθdecomposed(X: Matrix[Double], y: Matrix[Double], θ: Matrix[Double]) =
    (0 until X.rows).foldLeft(zeros(1, X.columns))(
      (m: Matrix[Double], i: Int) => {
        val xi = X.getRow(i)
        m + (xi ⨯ cost(xi, θ, y.valueAt(i, 0)))
      }
    ) / X.rows

  def dθ(X: Matrix[Double], y: Matrix[Double], θ: Matrix[Double]) =
    (0 until X.rows).foldLeft(zeros(1, X.columns))(
      (m: Matrix[Double], i: Int) => m + (X.getRow(i) ⨯ (h(X.getRow(i), θ) - y.valueAt(i, 0)))
    ) / X.rows

  def gradientDescentImmutable(X: Matrix[Double], y: Matrix[Double], θ: Matrix[Double], α: Double, iterations: Int) =
    (0 until iterations).foldLeft(θ)((θi: Matrix[Double], i: Int) => θi - (dθ(X, y, θi) * α))

  def gradientDescentMutable(X: Matrix[Double], y: Matrix[Double], θo: Matrix[Double], α: Double, iterations: Int) = {
    var θi = θo.dup
    var i = 0
    while (i < iterations) {
      θi -= (dθ(X, y, θi) * α)
      i = i + 1
    }
    θi
  }

  // non-unicode alias
  def dTheta(X: Matrix[Double], y: Matrix[Double], θ: Matrix[Double]) = dθ(X, y, θ)

}
