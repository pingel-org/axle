package axle.ml.distance

import scala.math.abs

import axle.matrix.JblasMatrixModule.Matrix
import axle.matrix.JblasMatrixModule.convertDouble
import spire.algebra.MetricSpace

object Manhattan extends MetricSpace[Matrix[Double], Double] {

  def distance(r1: Matrix[Double], r2: Matrix[Double]): Double = (r1 - r2).map(abs).toList.sum
}
