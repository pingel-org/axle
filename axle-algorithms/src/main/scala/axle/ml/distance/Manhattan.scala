package axle.ml.distance

import scala.math.abs

import axle.algebra.LinearAlgebra
import axle.syntax.linearalgebra._
import axle.syntax.endofunctor._
import spire.algebra.MetricSpace
import spire.implicits._

case class Manhattan[M](implicit la: LinearAlgebra[M, Double])
  extends MetricSpace[M, Double] {

  implicit val ring = la.ring
  implicit val e = la.endofunctor // TODO resolve implicitly
  
  def distance(r1: M, r2: M): Double = (r1 - r2).map(abs _).toList.sum
}
