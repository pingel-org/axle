package axle.bio

import spire.algebra._
import spire.implicits._
import Stream.{ cons, empty }
import axle.matrix.JblasMatrixModule._

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

  def computeH(A: String, B: String, mismatchPenalty: Int): Matrix[Int] = matrix[Int](
    A.length + 1,
    B.length + 1,
    0,
    (i: Int) => 0,
    (j: Int) => 0,
    (i: Int, j: Int, aboveleft: Int, left: Int, above: Int) => Vector(
      0,
      aboveleft + w(A(i - 1), B(j - 1), mismatchPenalty),
      above + mismatchPenalty,
      left + mismatchPenalty
    ).max
  )

  def alignStep(i: Int, j: Int, A: String, B: String, H: Matrix[Int], mismatchPenalty: Int): (Char, Char, Int, Int) =
    if (i > 0 && j > 0 && H(i, j) === H(i - 1, j - 1) + w(A(i - 1), B(j - 1), mismatchPenalty)) {
      (A(i - 1), B(j - 1), i - 1, j - 1)
    } else if (i > 0 && H(i, j) === H(i - 1, j) + mismatchPenalty) {
      (A(i - 1), gap, i - 1, j)
    } else {
      assert(j > 0 && H(i, j) === H(i, j - 1) + mismatchPenalty)
      (gap, B(j - 1), i, j - 1)
    }

  def _optimalAlignment(i: Int, j: Int, A: String, B: String, mismatchPenalty: Int, H: Matrix[Int]): Stream[(Char, Char)] =
    if (i > 0 || j > 0) {
      val (preA, preB, newI, newJ) = alignStep(i, j, A, B, H, mismatchPenalty)
      cons((preA, preB), _optimalAlignment(newI, newJ, A, B, mismatchPenalty, H))
    } else {
      empty
    }

  def optimalAlignment(A: String, B: String, mismatchPenalty: Int = defaultMismatchPenalty): (String, String) = {
    val H = computeH(A, B, mismatchPenalty)
    val (alignmentA, alignmentB) = _optimalAlignment(A.length, B.length, A, B, mismatchPenalty, H).unzip
    (alignmentA.reverse.mkString(""), alignmentB.reverse.mkString(""))
  }

  def metricSpace(mismatchPenalty: Int = defaultMismatchPenalty): MetricSpace[String, Int] = new SmithWatermanMetricSpace(mismatchPenalty)

  class SmithWatermanMetricSpace(mismatchPenalty: Int) extends MetricSpace[String, Int] {
    
    def distance(s1: String, s2: String): Int = {
      val H = computeH(s1, s2, mismatchPenalty)
      H(s1.length, s2.length)
    }

  }

}
