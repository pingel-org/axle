package org.pingel.axle.matrix

object LinearRegression extends LinearRegression()

class LinearRegression {

  import org.pingel.axle.matrix.DoubleJblasMatrixFactory._

  def normalEquation(X: M, y: M) = (X.t ⨯ X).inv ⨯ X.t ⨯ y

  def scaleColumns(X: M) = {
    val colMins = X.columnMins
    val colRanges = X.columnMaxs - colMins
    val scaled = (diag(colRanges).inv ⨯ X.subRowVector(colMins).t).t
    (scaled, colMins, colRanges)
  }

  def h(xi: M, θ: M) = xi ⨯ θ

  def dTheta(X: M, y: M, θ: M) = (0 until X.rows).foldLeft(zeros(1, X.columns))(
    (m: M, i: Int) => m + (X.getRow(i) ⨯ (h(X.getRow(i), θ) - y.valueAt(i, 0)))
  ) / X.rows

  def dθ(X: M, y: M, θ: M) = dTheta(X, y, θ)

  def gradientDescent(X: M, y: M, θ: M, α: Double, iterations: Int) =
    (0 until iterations).foldLeft(θ)((θi: M, i: Int) => θi - (dθ(X, y, θi) * α))

}