package org.pingel.axle.matrix

object LinearRegression extends LinearRegression()

class LinearRegression {

  import org.pingel.axle.matrix.DoubleJblasMatrixFactory._

  def normalEquation(X: Matrix, y: Matrix) = (X.t ⨯ X).inv ⨯ X.t ⨯ y

  def scaleColumns(X: Matrix) = {
    val colMins = X.columnMins
    val colRanges = X.columnMaxs - colMins
    val scaled = (diag(colRanges).inv ⨯ X.subRowVector(colMins).t).t
    (scaled, colMins, colRanges)
  }

  def h(xi: Matrix, θ: Matrix) = xi ⨯ θ

  def cost(xi: Matrix, θ: Matrix, yi: Double) = h(xi, θ) - yi

  def dθdecomposed(X: Matrix, y: Matrix, θ: Matrix) =
    (0 until X.rows).foldLeft(zeros(1, X.columns))(
      (m: Matrix, i: Int) => {
        val xi = X.getRow(i)
        val yi = y.valueAt(i, 0)
        m + (xi ⨯ cost(xi, θ, yi))
      }
    ) / X.rows

  def dθ(X: Matrix, y: Matrix, θ: Matrix) =
    (0 until X.rows).foldLeft(zeros(1, X.columns))(
      (m: Matrix, i: Int) => m + (X.getRow(i) ⨯ (h(X.getRow(i), θ) - y.valueAt(i, 0)))
    ) / X.rows

  def gradientDescentImmutable(X: Matrix, y: Matrix, θ: Matrix, α: Double, iterations: Int) =
    (0 until iterations).foldLeft(θ)((θi: Matrix, i: Int) => θi - (dθ(X, y, θi) * α))

  def gradientDescentMutable(X: Matrix, y: Matrix, θo: Matrix, α: Double, iterations: Int) = {
    var θi = θo.dup
    var i = 0
    while (i < iterations) {
      θi -= (dθ(X, y, θi) * α)
      i = i + 1
    }
    θi
  }

  // non-unicode alias
  def dTheta(X: Matrix, y: Matrix, θ: Matrix) = dθ(X, y, θ)

}
