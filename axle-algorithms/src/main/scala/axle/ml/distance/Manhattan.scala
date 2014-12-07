package axle.ml.distance

import scala.math.abs

import axle.algebra.Matrix
import axle.syntax.matrix.matrixOps
import spire.algebra.MetricSpace

case class Manhattan[M[_]: Matrix]() extends MetricSpace[M[Double], Double] {

  def distance(r1: M[Double], r2: M[Double]): Double = (r1 - r2).map(abs).toList.sum
}
