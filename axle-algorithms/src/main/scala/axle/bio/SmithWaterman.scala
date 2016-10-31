package axle.bio

import scala.Vector
import scala.collection.immutable.Stream
import scala.collection.immutable.Stream.cons
import scala.collection.immutable.Stream.empty

import axle.algebra.LinearAlgebra
import axle.algebra.Finite
import axle.algebra.FromStream
import axle.algebra.Indexed

import spire.algebra.Eq
import spire.algebra.MetricSpace
import spire.algebra.Order
import spire.algebra.Ring

import spire.compat.ordering
import spire.implicits.additiveGroupOps
import spire.implicits.additiveSemigroupOps
import spire.implicits.eqOps
import spire.implicits.partialOrderOps
import axle.syntax.finite.finiteOps
import axle.syntax.indexed.indexedOps
import axle.syntax.linearalgebra.matrixOps

/**
 *
 * http://en.wikipedia.org/wiki/Smith-Waterman_algorithm
 *
 */

object SmithWaterman {

  object Standard {

    def w(x: Char, y: Char, mismatchPenalty: Int): Int =
      if (x != y) {
        mismatchPenalty
      } else {
        2 // also see NeedlemanWunsch.Default.similarity
      }

    val mismatchPenalty = -1

    val gap = '-'

  }

  /**
   *
   * Computes the "H" matrix for two DNA sequences, A and B
   *
   * Same as Needleman-Wunsch's F matrix, except that all entries
   * in the matrix are non-negative.
   *
   */

  def computeH[S, C, M, I: Ring, V: Ring: Order](
    A: S,
    B: S,
    w: (C, C, V) => V,
    mismatchPenalty: V)(
      implicit la: LinearAlgebra[M, I, I, V],
      indexed: Indexed[S, I, C],
      finite: Finite[S, I]): M = {

    val iOne = Ring[I].one
    val vZero = Ring[V].zero

    la.matrix(
      A.size + iOne,
      B.size + iOne,
      vZero,
      (i: I) => vZero,
      (j: I) => vZero,
      (i: I, j: I, aboveleft: V, left: V, above: V) => Vector(
        vZero,
        aboveleft + w(A.at(i - iOne), B.at(j - iOne), mismatchPenalty),
        above + mismatchPenalty,
        left + mismatchPenalty).max)
  }

  def alignStep[S, C, M, I: Ring: Order, V: Ring: Order: Eq](
    i: I,
    j: I,
    A: S,
    B: S,
    w: (C, C, V) => V,
    H: M,
    mismatchPenalty: V,
    gap: C)(
      implicit la: LinearAlgebra[M, I, I, V],
      indexed: Indexed[S, I, C]): (C, C, I, I) = {

    val iZero = Ring[I].zero
    val iOne = Ring[I].one

    if (i > iZero && j > iZero && (H.get(i, j) === H.get(i - iOne, j - iOne) + w(A.at(i - iOne), B.at(j - iOne), mismatchPenalty))) {
      (A.at(i - iOne), B.at(j - iOne), i - iOne, j - iOne)
    } else if (i > 0 && H.get(i, j) === H.get(i - iOne, j) + mismatchPenalty) {
      (A.at(i - iOne), gap, i - iOne, j)
    } else {
      assert(j > 0 && H.get(i, j) === H.get(i, j - iOne) + mismatchPenalty)
      (gap, B.at(j - iOne), i, j - iOne)
    }
  }

  def _optimalAlignment[S, C, M, I: Ring: Order, V: Ring: Order](
    i: I,
    j: I,
    A: S,
    B: S,
    w: (C, C, V) => V,
    mismatchPenalty: V,
    gap: C,
    H: M)(
      implicit la: LinearAlgebra[M, I, I, V],
      indexed: Indexed[S, I, C]): Stream[(C, C)] =
    if (i > 0 || j > 0) {
      val (preA, preB, newI, newJ) = alignStep[S, C, M, I, V](i, j, A, B, w, H, mismatchPenalty, gap)
      cons((preA, preB), _optimalAlignment[S, C, M, I, V](newI, newJ, A, B, w, mismatchPenalty, gap, H))
    } else {
      empty
    }

  def optimalAlignment[S, C, M, I: Ring: Order, V: Ring: Order](
    A: S,
    B: S,
    w: (C, C, V) => V,
    mismatchPenalty: V,
    gap: C)(
      implicit la: LinearAlgebra[M, I, I, V],
      indexed: Indexed[S, I, C],
      finite: Finite[S, I],
      fs: FromStream[S, C]): (S, S) = {

    val H = computeH[S, C, M, I, V](A, B, w, mismatchPenalty)

    val (alignmentA, alignmentB) = _optimalAlignment[S, C, M, I, V](A.size, B.size, A, B, w, mismatchPenalty, gap, H).unzip

    (fs.fromStream(alignmentA.reverse), fs.fromStream(alignmentB.reverse))
  }

}

case class SmithWatermanMetricSpace[S, C, M, I: Ring, V: Ring: Order](
    w: (C, C, V) => V,
    mismatchPenalty: V)(
        implicit la: LinearAlgebra[M, I, I, V],
        finite: Finite[S, I],
        indexed: Indexed[S, I, C]) extends MetricSpace[S, V] {

  def distance(s1: S, s2: S): V = {

    val H = SmithWaterman.computeH[S, C, M, I, V](s1, s2, w, mismatchPenalty)

    H.get(s1.size, s2.size)
  }

}

object SmithWatermanMetricSpace {

  def common[U[_], C, M, I: Ring, V: Ring: Order](
    w: (C, C, V) => V,
    mismatchPenalty: V)(
      implicit la: LinearAlgebra[M, I, I, V],
      finite: Finite[U[C], I],
      indexed: Indexed[U[C], I, C]) =
    SmithWatermanMetricSpace[U[C], C, M, I, V](w, mismatchPenalty)
}