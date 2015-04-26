package axle.nlp

import scala.reflect.ClassTag

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

case class Levenshtein[E: Eq: ClassTag, M, I: Ring, D: Order: Field, S[_]]()(
  implicit la: LinearAlgebra[M, I, I, D],
  idx: Indexed[S, I],
  finite: Finite[S, I])
  extends MetricSpace[S[E], D] {

  def distance(s1: S[E], s2: S[E]): D = {

    val i1 = Ring[I].one
    val d0 = Field[D].zero
    val d1 = Field[D].one

    val lenStr1: I = s1.size
    val lenStr2: I = s2.size

    val d = la.matrix(
      lenStr1 + i1,
      lenStr2 + i1,
      d1,
      (r: I) => d1,
      (c: I) => d1,
      (r: I, c: I, diag: D, left: D, top: D) =>
        min(
          left + d1,
          top + d1,
          diag + (if (s1.at(r - i1) === s2.at(c - i1)) d0 else d1)))

    d.get(lenStr1, lenStr2)
  }

  def min(nums: D*): D = nums.min

}
