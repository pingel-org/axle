package axle.ml


trait Regression {

  import axle.matrix.JblasMatrixFactory._
  type M[T] = JblasMatrix[T] // TODO: generalize

  def scaleColumns(X: M[Double]) = {
    val colMins = X.columnMins
    val colRanges = X.columnMaxs - colMins
    val scaled = (diag(colRanges).inv ⨯ X.subRowVector(colMins).t).t
    (scaled, colMins, colRanges)
  }

}