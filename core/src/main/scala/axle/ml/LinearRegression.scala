package axle.ml

object LinearRegression extends LinearRegression()
 
trait LinearRegression {

  import axle.matrix.JblasMatrixFactory._ // TODO: generalize
  type M[T] = JblasMatrix[T]

  def normalEquation(X: M[Double], y: M[Double]) = (X.t ⨯ X).inv ⨯ X.t ⨯ y

  def scaleColumns(X: M[Double]) = {
    val colMins = X.columnMins
    val colRanges = X.columnMaxs - colMins
    val scaled = (diag(colRanges).inv ⨯ X.subRowVector(colMins).t).t
    (scaled, colMins, colRanges)
  }

  def h(xi: M[Double], θ: M[Double]) = xi ⨯ θ

  def cost(xi: M[Double], θ: M[Double], yi: Double) = h(xi, θ) - yi

  def dθ(X: M[Double], y: M[Double], θ: M[Double]) = (0 until X.rows)
    .foldLeft(zeros[Double](1, X.columns))(
      (m: M[Double], i: Int) => { m + (X.getRow(i) ⨯ (h(X.getRow(i), θ) - y.valueAt(i, 0))) }
    ) / X.rows

  def gradientDescentImmutable(X: M[Double], y: M[Double], θ: M[Double], α: Double, iterations: Int) =
    (0 until iterations).foldLeft(θ)((θi: JblasMatrix[Double], i: Int) => θi - (dθ(X, y, θi) * α))

  def gradientDescentMutable(X: M[Double], y: M[Double], θo: M[Double], α: Double, iterations: Int) = {
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
