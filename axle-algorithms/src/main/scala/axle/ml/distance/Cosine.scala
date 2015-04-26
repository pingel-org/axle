package axle.ml.distance

import axle.algebra.LinearAlgebra
import axle.algebra.Zero
import axle.syntax.linearalgebra.matrixOps
import spire.algebra.Field
import spire.algebra.InnerProductSpace
import spire.algebra.MultiplicativeMonoid
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

case class Cosine[M, R: Zero: MultiplicativeMonoid, C: Zero, V: Field](n: C)(
  implicit la: LinearAlgebra[M, R, C, V])
  extends InnerProductSpace[M, V] {

  implicit val ring = la.ring
  implicit val module = la.module

  def negate(x: M): M = x.negate

  def zero: M = la.zeros(implicitly[MultiplicativeMonoid[R]].one, n)

  def plus(x: M, y: M): M = la.ring.plus(x, y)

  def timesl(r: V, v: M): M = v :* r

  def scalar: Field[V] = Field[V]

  def dot(v: M, w: M): V = v.mulPointwise(w).rowSums.scalar

}
