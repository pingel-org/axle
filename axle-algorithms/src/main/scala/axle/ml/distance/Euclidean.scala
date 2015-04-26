package axle.ml.distance

import axle.algebra.LinearAlgebra
import axle.algebra.Zero
import axle.syntax.linearalgebra.matrixOps
import spire.algebra.Field
import spire.algebra.MultiplicativeMonoid
import spire.algebra.NRoot
import spire.algebra.NormedVectorSpace
import spire.implicits.nrootOps

/**
 * Euclidean space
 *
 *   n = num columns in row vectors
 *
 *   distance(r1, r2) = norm(r1 - r2)
 *
 */

case class Euclidean[M, R: Zero: MultiplicativeMonoid, C: Zero, V: Field: NRoot](n: C)(
  implicit la: LinearAlgebra[M, R, C, V])
  extends NormedVectorSpace[M, V] {

  def negate(x: M): M = la.negate(x)

  def zero: M = la.zeros(implicitly[MultiplicativeMonoid[R]].one, n)

  def plus(x: M, y: M): M = la.ring.plus(x, y)

  def timesl(r: V, v: M): M = la.module.timesl(r, v)

  def scalar: Field[V] = Field[V]

  def norm(r: M): V = r.mulPointwise(r).rowSums.scalar.sqrt

}
