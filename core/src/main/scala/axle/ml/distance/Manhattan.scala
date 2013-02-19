package axle.ml.distance

import spire.algebra._
import spire.math._
import spire.implicits._
import axle.matrix.JblasMatrixModule._
import math.abs

object Manhattan extends MetricSpace[Matrix[Double], Double] {

  def distance(r1: Matrix[Double], r2: Matrix[Double]) = (r1 - r2).map(abs(_)).toList.sum
}
