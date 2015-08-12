package axle.nlp

import axle.algebra.Finite
import axle.algebra.Indexed
import axle.algebra.LinearAlgebra
import axle.syntax.finite.finiteOps
import axle.syntax.indexed.indexedOps
import axle.syntax.linearalgebra.matrixOps
import spire.algebra.Eq
import spire.algebra.Field
import spire.algebra.MetricSpace
import spire.algebra.Order
import spire.algebra.Ring
import spire.compat.ordering
import spire.implicits.additiveGroupOps
import spire.implicits.additiveSemigroupOps
import spire.implicits.eqOps

/**
 * Based on the Scala implementation of
 *
 * http://en.wikibooks.org/wiki/Algorithm_Implementation/Strings/Levenshtein_distance
 *
 */

case class Levenshtein[S, E: Eq, M, I: Ring: Order]()(
  implicit la: LinearAlgebra[M, I, I, I],
  idx: Indexed[S, I, E],
  finite: Finite[S, I])
    extends MetricSpace[S, I] {

  def distance(s1: S, s2: S): I = {

    val i0 = Ring[I].zero
    val i1 = Ring[I].one

    val lenStr1: I = s1.size
    val lenStr2: I = s2.size

    val d = la.matrix(
      lenStr1 + i1,
      lenStr2 + i1,
      i0,
      (r: I) => r,
      (c: I) => c,
      (r: I, c: I, diag: I, left: I, top: I) =>
        min(
          left + i1,
          top + i1,
          diag + (if (s1.at(r - i1) === s2.at(c - i1)) i0 else i1)))

    d.get(lenStr1, lenStr2)
  }

  def min(nums: I*): I = nums.min

}

object Levenshtein {

  def common[U[_], E: Eq, M, I: Ring: Order]()(
    implicit la: LinearAlgebra[M, I, I, I],
    idx: Indexed[U[E], I, E],
    finite: Finite[U[E], I]) =
    Levenshtein[U[E], E, M, I]()
}