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

  def dθ(X: Matrix, y: Matrix, θ: Matrix) = (0 until X.rows).foldLeft(zeros(1, X.columns))(
    (m: Matrix, i: Int) => m + (X.getRow(i) ⨯ (h(X.getRow(i), θ) - y.valueAt(i, 0)))
  ) / X.rows

  def gradientDescent(X: Matrix, y: Matrix, θ: Matrix, α: Double, iterations: Int) =
    (0 until iterations).foldLeft(θ)((θi: Matrix, i: Int) => θi - (dθ(X, y, θi) * α))

  // non-unicode alias
  def dTheta(X: Matrix, y: Matrix, θ: Matrix) = dθ(X, y, θ)
    
}