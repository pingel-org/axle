package axle.ml

import axle.matrix._
import axle._

trait DistanceFunctionModule extends JblasMatrixModule {

  import math.{ abs, sqrt }

  def norm(row: Matrix[Double]): Double = sqrt((0 until row.columns).map(i => square(row(0, i))).sum)

  def dotProduct(row1: Matrix[Double], row2: Matrix[Double]): Double = ???

  // trait DistanceFunction extends ((Matrix[Double], Matrix[Double]) => Double)
  // TODO: apply should assert(r1.isRowVector && r2.isRowVector && r1.length == r2.length)

  def euclidian() = (r1: Matrix[Double], r2: Matrix[Double]) => norm(r1 - r2)

  def manhattan() = (r1: Matrix[Double], r2: Matrix[Double]) => (r1 - r2).map(abs(_)).toList.sum

  def cosine() = (r1: Matrix[Double], r2: Matrix[Double]) => 1.0 - (dotProduct(r1, r2) / (norm(r1) * norm(r2)))

}
