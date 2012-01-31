package org.pingel.axle.matrix

object LinearRegression extends LinearRegression()

class LinearRegression {

  import org.pingel.axle.matrix.DoubleJblasMatrixFactory._

  // def normalEquation(X: Matrix[Double], y: Matrix[Double]): Matrix[Double] = (X.t ⨯ X).inv ⨯ X.t ⨯ y

  def scale(X: Matrix[Double]): (Matrix[Double], Matrix[Double], Matrix[Double]) = {
    val colMins = X.columnMins
    val colRanges = X.columnMaxs - colMins
    val scaled = (diag(colRanges.inv) ⨯ X.subRowVector(colMins).t).t
    (scaled, colMins, colRanges)
  }

  def h(xi: Matrix[Double], θ: Matrix[Double]): Matrix[Double] = xi // TODO ⨯ θ

  def dθ(X: Matrix[Double], y: Matrix[Double], θ: Matrix[Double]): Matrix[Double] =
    (0 until X.rows)
      .foldLeft(zeros(1, X.columns))(
        (m: Matrix[Double], i: Int) => { m + (X.getRow(i) ⨯ (h(X.getRow(i), θ) - y.valueAt(i, 1))) }
      )./(X.rows)

  def gradientDescent(X: Matrix[Double], y: Matrix[Double], θ: Matrix[Double], α: Double, iterations: Int): Matrix[Double] =
    (0 until iterations).foldLeft(θ)((θi: Matrix[Double], i: Int) => θi - (dθ(X, y, θi) * α))

}