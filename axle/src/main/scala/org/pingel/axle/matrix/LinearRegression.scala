package org.pingel.axle.matrix

object LinearRegression extends LinearRegression()

class LinearRegression {

  import org.pingel.axle.matrix._
  import org.pingel.axle.matrix.DoubleJblasMatrixFactory._

//  type DoubleMatrix = DoubleJblasMatrixFactoryClass#DoubleJblasMatrixImpl
//  type BooleanMatrix = BooleanJblasMatrixFactoryClass#BooleanJblasMatrixImpl

  type DoubleMatrix = Matrix { type T = Double }
  type BooleanMatrix = Matrix { type T = Boolean }

  def normalEquation(X: DoubleMatrix, y: DoubleMatrix) = (X.t ⨯ X).inv ⨯ X.t ⨯ y

  def scaleColumns(X: DoubleMatrix): (DoubleMatrix, DoubleMatrix, DoubleMatrix) = {
    val colMins: DoubleMatrix = X.columnMins
    val colRanges: DoubleMatrix = X.columnMaxs - colMins
    val scaled: DoubleMatrix = (colRanges.diag().inv ⨯ X.subRowVector(colMins).t).t
    (scaled, colMins, colRanges)
  }

  def h(xi: DoubleMatrix, θ: DoubleMatrix): DoubleMatrix = xi ⨯ θ

  def cost(xi: DoubleMatrix, θ: DoubleMatrix, yi: Double): DoubleMatrix = h(xi, θ) - yi

  def dθdecomposed(X: DoubleMatrix, y: DoubleMatrix, θ: DoubleMatrix): DoubleMatrix =
    (0 until X.rows).foldLeft(zeros(1, X.columns))(
      (m: DoubleMatrix, i: Int) => {
        val xi = X.getRow(i)
        val c: DoubleMatrix = (xi ⨯ cost(xi, θ, y.valueAt(i, 0)))
        m + c
      }
    ) / X.rows

  def dθ(X: DoubleMatrix, y: DoubleMatrix, θ: DoubleMatrix): DoubleMatrix =
    (0 until X.rows).foldLeft(zeros(1, X.columns))(
      (m: DoubleMatrix, i: Int) => {
        m + (X.getRow(i) ⨯ (h(X.getRow(i), θ) - y.valueAt(i, 0)))
      }
    ) / X.rows

  def gradientDescentImmutable(X: DoubleMatrix, y: DoubleMatrix, θ: DoubleMatrix, α: Double, iterations: Int): DoubleMatrix =
    (0 until iterations).foldLeft(θ)((θi: DoubleMatrix, i: Int) => θi - (dθ(X, y, θi) * α))

  def gradientDescentMutable(X: DoubleMatrix, y: DoubleMatrix, θo: DoubleMatrix, α: Double, iterations: Int): DoubleMatrix = {
    var θi = θo.dup
    var i = 0
    while (i < iterations) {
      θi -= (dθ(X, y, θi) * α)
      i = i + 1
    }
    θi
  }

  // non-unicode alias
  def dTheta(X: DoubleMatrix, y: DoubleMatrix, θ: DoubleMatrix) = dθ(X, y, θ)

}
