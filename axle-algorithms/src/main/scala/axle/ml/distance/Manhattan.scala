package axle.ml.distance

import scala.math.abs

import axle.algebra.LinearAlgebra
import axle.syntax.linearalgebra._
import spire.algebra.MetricSpace

case class Manhattan[M](implicit la: LinearAlgebra[M, Double])
  extends MetricSpace[M, Double] {

  def distance(r1: M, r2: M): Double = (r1 - r2).map(abs _).toList.sum
}
