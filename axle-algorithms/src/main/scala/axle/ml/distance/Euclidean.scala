package axle.ml.distance

import axle.algebra.LinearAlgebra
import axle.algebra.Zero
import axle.syntax.linearalgebra.matrixOps
import spire.algebra.Field
import spire.algebra.AdditiveAbGroup
import spire.algebra.MultiplicativeMonoid
import spire.algebra.NRoot
import spire.algebra.MetricSpace
import spire.implicits._

/**
 * Euclidean space
 *
 * @param n = num columns in row vectors
 *
 * http://en.wikipedia.org/wiki/Euclidean_space
 *
 */

case class Euclidean[M: AdditiveAbGroup, R: Zero: MultiplicativeMonoid, C: Zero, D: Field: NRoot](n: C)(
  implicit la: LinearAlgebra[M, R, C, D])
    extends MetricSpace[M, D] {

  def dot(v: M, w: M): D =
    v.mulPointwise(w).rowSums.scalar

  def distance(x: M, y: M): D = {
    val diff: M = x - y
    dot(diff, diff).sqrt
  }

}
