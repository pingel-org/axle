package axle.ml.distance

import axle.algebra.LinearAlgebra
import axle.syntax.linearalgebra.matrixOps
import spire.algebra.Field
import spire.algebra.InnerProductSpace
import spire.implicits.DoubleAlgebra
import spire.implicits.IntAlgebra
import spire.implicits.moduleOps

/**
 *
 * Cosine space
 *
 * @param n = num columns in row vectors
 *
 * distance(r1, r2) = 1.0 - abs(rvDot(r1, r2) / (norm(r1) * norm(r2)))
 *
 * TODO: distance calcs could assert(r1.isRowVector && r2.isRowVector && r1.length === r2.length)
 *
 */

case class Cosine[M](n: Int)(implicit la: LinearAlgebra[M, Int, Int, Double])
  extends InnerProductSpace[M, Double] {

  implicit val ring = la.ring
  implicit val module = la.module

  def negate(x: M): M = x.negate

  def zero: M = la.zeros(1, n)

  def plus(x: M, y: M): M = la.ring.plus(x, y)

  def timesl(r: Double, v: M): M = v :* r

  def scalar: Field[Double] = DoubleAlgebra

  def dot(v: M, w: M): Double = v.mulPointwise(w).rowSums.scalar

}
