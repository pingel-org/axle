package org.pingel.axle.matrix

object LinearRegression extends LinearRegression()

class LinearRegression {

  import org.pingel.axle.matrix.JblasMatrixFactory._

  def normalEquation(X: JblasMatrix[Double], y: JblasMatrix[Double]) = (X.t ⨯ X).inv ⨯ X.t ⨯ y

  def scaleColumns(X: JblasMatrix[Double]): (JblasMatrix[Double], JblasMatrix[Double], JblasMatrix[Double]) = {
    val colMins: JblasMatrix[Double] = X.columnMins
    val colRanges: JblasMatrix[Double] = X.columnMaxs - colMins
    val scaled: JblasMatrix[Double] = (colRanges.diag().inv ⨯ X.subRowVector(colMins).t).t
    (scaled, colMins, colRanges)
  }

  def h(xi: JblasMatrix[Double], θ: JblasMatrix[Double]): JblasMatrix[Double] = xi ⨯ θ

  def cost(xi: JblasMatrix[Double], θ: JblasMatrix[Double], yi: Double): JblasMatrix[Double] = h(xi, θ) - yi

  def dθdecomposed(X: JblasMatrix[Double], y: JblasMatrix[Double], θ: JblasMatrix[Double]): JblasMatrix[Double] =
    (0 until X.rows).foldLeft(zeros[Double](1, X.columns))(
      (m: JblasMatrix[Double], i: Int) => {
        val xi = X.getRow(i)
        val c: JblasMatrix[Double] = (xi ⨯ cost(xi, θ, y.valueAt(i, 0)))
        m + c
      }
    ) / X.rows

  def dθ(X: JblasMatrix[Double], y: JblasMatrix[Double], θ: JblasMatrix[Double]): JblasMatrix[Double] =
    (0 until X.rows).foldLeft(zeros[Double](1, X.columns))(
      (m: JblasMatrix[Double], i: Int) => {
        m + (X.getRow(i) ⨯ (h(X.getRow(i), θ) - y.valueAt(i, 0)))
      }
    ) / X.rows

  def gradientDescentImmutable(X: JblasMatrix[Double], y: JblasMatrix[Double], θ: JblasMatrix[Double], α: Double, iterations: Int): JblasMatrix[Double] =
    (0 until iterations).foldLeft(θ)((θi: JblasMatrix[Double], i: Int) => θi - (dθ(X, y, θi) * α))

  def gradientDescentMutable(X: JblasMatrix[Double], y: JblasMatrix[Double], θo: JblasMatrix[Double], α: Double, iterations: Int): JblasMatrix[Double] = {
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
