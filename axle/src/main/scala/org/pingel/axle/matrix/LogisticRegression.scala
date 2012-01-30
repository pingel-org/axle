package org.pingel.axle.matrix

class LogisticRegression {

  import org.pingel.axle.matrix.DoubleJblasMatrixFactory._

  def normalEquation(X: Matrix[Double], y: Matrix[Double]): Matrix[Double] = (X.t ⨯ X).inv ⨯ X.t ⨯ y

  def scaleX(X: Matrix[Double]): Matrix[Double] = {
    val xRange = X.columnMaxs - X.columnMins
    xRange.setValueAt(0, 0, 1)
    val Xfloored = X - X.columnMins() // TODO: subtract this row vector from all rows in X
    val Xscaled = (diag(xRange.inv) ⨯ Xfloored.t).t
    Xscaled.setValueAt(allRows, 0, 1)
    Xscaled
  }

  def scaleY(y: Matrix[Double]): Matrix[Double] = {
    val yFloored = y - y.min
    yFloored / yFloored.max
  }

  def h(xi: Matrix[Double], θ: Matrix[Double]): Matrix[Double] = xi ⨯ θ

  def gradientDescentUpdate(X: Matrix[Double], y: Matrix[Double], θ: Matrix[Double]): Matrix[Double] =
    (0 until X.rows)
      .foldLeft(zeros(1, X.columns))(
        (m: Matrix[Double], i: Int) => { m + (X.getRow(i) ⨯ (h(X.getRow(i), θ) - y.valueAt(i, 1))) }
      )
      ./(X.rows)

  def gradientDescent(X: Matrix[Double], y: Matrix[Double], θ: Matrix[Double], α: Double, iterations: Int): Matrix[Double] = 
    (0 until iterations)
      .foldLeft(θ)(
        (θi: Matrix[Double], i: Int) => {
          val dθ = gradientDescentUpdate(X, y, θi)
          θi - (dθ * α)
        }
      )

}