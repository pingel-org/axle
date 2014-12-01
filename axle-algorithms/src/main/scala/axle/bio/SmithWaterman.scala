package axle.bio

import scala.Vector
import scala.collection.immutable.Stream.cons
import scala.collection.immutable.Stream.empty

import axle.algebra.Matrix
import spire.algebra.MetricSpace
import spire.implicits.IntAlgebra
import spire.implicits.eqOps
import axle.syntax.matrix._

/**
 *
 * http://en.wikipedia.org/wiki/Smith-Waterman_algorithm
 *
 */

object SmithWaterman {

  def w(x: Char, y: Char, mismatchPenalty: Int): Int =
    if (x != y) {
      mismatchPenalty
    } else {
      2 // also see NeedlemanWunsch.S(x, y)
    }

  val defaultMismatchPenalty = -1

  val gap = '-'

  /**
   *
   * Computes the "H" matrix for two DNA sequences, A and B
   *
   * Same as Needleman-Wunsch's F matrix, except that all entries
   * in the matrix are non-negative.
   *
   */

  def computeH[M[_]](A: String, B: String, mismatchPenalty: Int)(implicit ev: Matrix[M]): M[Int] =
    ev.matrix[Int](
      A.length + 1,
      B.length + 1,
      0,
      (i: Int) => 0,
      (j: Int) => 0,
      (i: Int, j: Int, aboveleft: Int, left: Int, above: Int) => Vector(
        0,
        aboveleft + w(A(i - 1), B(j - 1), mismatchPenalty),
        above + mismatchPenalty,
        left + mismatchPenalty).max)

  def alignStep[M[_]: Matrix](i: Int, j: Int, A: String, B: String, H: M[Int], mismatchPenalty: Int): (Char, Char, Int, Int) = {
    if (i > 0 && j > 0 && H.get(i, j) === H.get(i - 1, j - 1) + w(A(i - 1), B(j - 1), mismatchPenalty)) {
      (A(i - 1), B(j - 1), i - 1, j - 1)
    } else if (i > 0 && H.get(i, j) === H.get(i - 1, j) + mismatchPenalty) {
      (A(i - 1), gap, i - 1, j)
    } else {
      assert(j > 0 && H.get(i, j) === H.get(i, j - 1) + mismatchPenalty)
      (gap, B(j - 1), i, j - 1)
    }
  }

  def _optimalAlignment[M[_]: Matrix](i: Int, j: Int, A: String, B: String, mismatchPenalty: Int, H: M[Int]): scala.collection.immutable.Stream[(Char, Char)] =
    if (i > 0 || j > 0) {
      val (preA, preB, newI, newJ) = alignStep(i, j, A, B, H, mismatchPenalty)
      cons((preA, preB), _optimalAlignment(newI, newJ, A, B, mismatchPenalty, H))
    } else {
      empty
    }

  def optimalAlignment[M[_]: Matrix](A: String, B: String, mismatchPenalty: Int = defaultMismatchPenalty): (String, String) = {
    val H = computeH(A, B, mismatchPenalty)
    val (alignmentA, alignmentB) = _optimalAlignment(A.length, B.length, A, B, mismatchPenalty, H).unzip
    (alignmentA.reverse.mkString(""), alignmentB.reverse.mkString(""))
  }

  def metricSpace[M[_]: Matrix](mismatchPenalty: Int = defaultMismatchPenalty): MetricSpace[String, Int] =
    SmithWatermanMetricSpace(mismatchPenalty)

  case class SmithWatermanMetricSpace[M[_]: Matrix](mismatchPenalty: Int) extends MetricSpace[String, Int] {

    def distance(s1: String, s2: String): Int = {
      val H = computeH(s1, s2, mismatchPenalty)
      H.get(s1.length, s2.length)
    }

  }

}
