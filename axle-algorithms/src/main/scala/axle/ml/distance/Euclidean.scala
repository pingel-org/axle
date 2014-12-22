package axle.ml.distance

import scala.math.sqrt

import axle.algebra.LinearAlgebra
import axle.syntax.linearalgebra._
import spire.algebra.Field
import spire.algebra.NormedVectorSpace
import spire.implicits.DoubleAlgebra

/**
 * Euclidean space
 *
 *   n = num columns in row vectors
 *
 *   distance(r1, r2) = norm(r1 - r2)
 *
 */

case class Euclidian[M](n: Int)(implicit la: LinearAlgebra[M, Double])
  extends NormedVectorSpace[M, Double] {

  def negate(x: M): M = la.negate(x)

  def zero: M = la.zeros(1, n)

  def plus(x: M, y: M): M = la.ring.plus(x, y)

  def timesl(r: Double, v: M): M = la.module.timesl(r, v)

  def scalar: Field[Double] = DoubleAlgebra

  def norm(r: M): Double = sqrt(r.mulPointwise(r).rowSums.scalar)

}
