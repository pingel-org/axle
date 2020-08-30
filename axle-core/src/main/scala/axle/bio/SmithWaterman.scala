package axle.bio

import scala.Vector

import cats.implicits._
import cats.kernel.Eq
import cats.kernel.Order

import spire.algebra.Ring
import spire.implicits.additiveGroupOps
import spire.implicits.additiveSemigroupOps

import axle.algebra.Finite
import axle.algebra.FromStream
import axle.algebra.Indexed
import axle.algebra.LinearAlgebra
import axle.algebra.SimilaritySpace
import axle.syntax.finite.finiteOps
import axle.syntax.indexed.indexedOps
import axle.syntax.linearalgebra.matrixOps

/**
 *
 * http://en.wikipedia.org/wiki/Smith-Waterman_algorithm
 *
 */

object SmithWaterman {

  /**
   *
   * Computes the "H" matrix for two DNA sequences, A and B
   *
   * Same as Needleman-Wunsch's F matrix, except that all entries
   * in the matrix are non-negative.
   *
   */

  def computeH[S[_], C, M, I: Ring, V: Ring: Order](
    A:               S[C],
    B:               S[C],
    w:               (C, C, V) => V,
    mismatchPenalty: V)(
    implicit
    la:      LinearAlgebra[M, I, I, V],
    indexed: Indexed[S, I],
    finite:  Finite[S, I]): M = {

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

  def alignStep[S[_], C, M, I: Ring: Order, V: Ring: Order: Eq](
    i:               I,
    j:               I,
    A:               S[C],
    B:               S[C],
    w:               (C, C, V) => V,
    H:               M,
    mismatchPenalty: V,
    gap:             C)(
    implicit
    la:      LinearAlgebra[M, I, I, V],
    indexed: Indexed[S, I]): (C, C, I, I) = {

    val iZero = Ring[I].zero
    val iOne = Ring[I].one

    if (i > iZero && j > iZero && (H.get(i, j) === H.get(i - iOne, j - iOne) + w(A.at(i - iOne), B.at(j - iOne), mismatchPenalty))) {
      (A.at(i - iOne), B.at(j - iOne), i - iOne, j - iOne)
    } else if (i > iZero && H.get(i, j) === H.get(i - iOne, j) + mismatchPenalty) {
      (A.at(i - iOne), gap, i - iOne, j)
    } else {
      assert(j > iZero && H.get(i, j) === H.get(i, j - iOne) + mismatchPenalty)
      (gap, B.at(j - iOne), i, j - iOne)
    }
  }

  def _optimalAlignment[S[_], C, M, I: Ring: Order, V: Ring: Order](
    i:               I,
    j:               I,
    A:               S[C],
    B:               S[C],
    w:               (C, C, V) => V,
    mismatchPenalty: V,
    gap:             C,
    H:               M)(
    implicit
    la:      LinearAlgebra[M, I, I, V],
    indexed: Indexed[S, I]): LazyList[(C, C)] = {
    val iZero = Ring[I].zero
    if (i > iZero || j > iZero) {
      val (preA, preB, newI, newJ) = alignStep[S, C, M, I, V](i, j, A, B, w, H, mismatchPenalty, gap)
      LazyList.cons((preA, preB), _optimalAlignment[S, C, M, I, V](newI, newJ, A, B, w, mismatchPenalty, gap, H))
    } else {
      LazyList.empty
    }
  }

  def optimalAlignment[S[_], C, M, I: Ring: Order, V: Ring: Order](
    A:               S[C],
    B:               S[C],
    w:               (C, C, V) => V,
    mismatchPenalty: V,
    gap:             C)(
    implicit
    la:      LinearAlgebra[M, I, I, V],
    indexed: Indexed[S, I],
    finite:  Finite[S, I],
    fs:      FromStream[S[C], C]): (S[C], S[C]) = {

    val H = computeH[S, C, M, I, V](A, B, w, mismatchPenalty)

    val (alignmentA, alignmentB) = _optimalAlignment[S, C, M, I, V](A.size, B.size, A, B, w, mismatchPenalty, gap, H).unzip

    (fs.fromStream(alignmentA.reverse), fs.fromStream(alignmentB.reverse))
  }

}

case class SmithWatermanSimilaritySpace[S[_], C, M, I: Ring, V: Ring: Order](
  w:               (C, C, V) => V,
  mismatchPenalty: V)(
  implicit
  la:      LinearAlgebra[M, I, I, V],
  finite:  Finite[S, I],
  indexed: Indexed[S, I]) extends SimilaritySpace[S[C], V] {

  def similarity(s1: S[C], s2: S[C]): V = {

    val H = SmithWaterman.computeH[S, C, M, I, V](s1, s2, w, mismatchPenalty)

    H.get(s1.size, s2.size)
  }

}
