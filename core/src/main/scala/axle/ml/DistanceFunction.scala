package axle.ml

import axle.square

object DistanceFunction {

  import math.{ abs, sqrt }
  import axle.matrix.JblasMatrixFactory._ // TODO: generalize
  type M[T] = JblasMatrix[T]

  trait DistanceFunction extends ((M[Double], M[Double]) => Double)

  // TODO: apply should assert(r1.isRowVector && r2.isRowVector && r1.length == r2.length)

  object EuclideanDistanceFunction extends DistanceFunction {
    def apply(r1: M[Double], r2: M[Double]): Double = {
      val dRow = r1 - r2
      sqrt((0 until r1.columns).map(i => square(dRow(0, i))).sum)
    }
  }

  object ManhattanDistanceFunction extends DistanceFunction {
    def apply(r1: M[Double], r2: M[Double]): Double = {
      val dRow = r1 - r2
      (0 until r1.columns).map(i => abs(dRow(0, i))).sum
    }
  }

  object CosineDistanceFunction extends DistanceFunction {
    def apply(r1: M[Double], r2: M[Double]): Double = {
      1.0 // TODO
    }
  }

}
