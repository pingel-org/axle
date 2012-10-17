package axle.ml

import axle.square

object DistanceFunction {

  import math.{ abs, sqrt }
  import axle.matrix.JblasMatrixFactory._ // TODO: generalize
  type M[T] = JblasMatrix[T]

  def norm(row: M[Double]): Double = sqrt((0 until row.columns).map(i => square(row(0, i))).sum)

  def dotProduct(row1: M[Double], row2: M[Double]): Double = 1.0

  trait DistanceFunction extends ((M[Double], M[Double]) => Double)

  // TODO: apply should assert(r1.isRowVector && r2.isRowVector && r1.length == r2.length)

  object EuclideanDistanceFunction extends DistanceFunction {
    def apply(r1: M[Double], r2: M[Double]): Double = norm(r1 - r2)
  }

  object ManhattanDistanceFunction extends DistanceFunction {
    def apply(r1: M[Double], r2: M[Double]): Double = (r1 - r2).map(abs(_)).toList.sum
  }

  object CosineDistanceFunction extends DistanceFunction {
    def apply(r1: M[Double], r2: M[Double]): Double = 1.0 - (dotProduct(r1, r2) / (norm(r1) * norm(r2)))
  }

}
