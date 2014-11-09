package axle.bio

import scala.Stream.cons
import scala.Stream.empty
import scala.Vector

import axle.matrix.MatrixModule
import spire.algebra.MetricSpace
import spire.implicits.CharAlgebra
import spire.implicits.IntAlgebra
import spire.implicits.eqOps

/**
 *
 * http://en.wikipedia.org/wiki/Needleman-Wunsch_algorithm
 *
 */

abstract class NeedlemanWunsch extends MatrixModule {

  /**
   * S is "similarity", computed by a fixed Int matrix
   *
   * S(a, b) === S(b, a)
   *
   */

  def S(x: Char, y: Char): Int = (x, y) match {
    case ('A', 'A') => 10
    case ('A', 'G') => -1
    case ('A', 'C') => -3
    case ('A', 'T') => -4
    case ('G', 'A') => -1
    case ('G', 'G') => 7
    case ('G', 'C') => -5
    case ('G', 'T') => -3
    case ('C', 'A') => -3
    case ('C', 'G') => -5
    case ('C', 'C') => 9
    case ('C', 'T') => 0
    case ('T', 'A') => -4
    case ('T', 'G') => -3
    case ('T', 'C') => 0
    case ('T', 'T') => 8
  }

  val gap = '-'
  val defaultGapPenalty = -5

  def alignmentScore(A: String, B: String, gapPenalty: Int = defaultGapPenalty): Int = {
    assert(A.length === B.length)
    (0 until A.length).map(i =>
      if (A(i) === gap || B(i) === gap) {
        gapPenalty
      } else {
        S(A(i), B(i))
      }
    ).sum
  }

  // ←
  // alignmentScore("AGACTAGTTAC", "CGA---GACGT")

  /**
   *
   * Computes the "F" matrix for two DNA sequences, A and B
   *
   */

  def computeF(A: String, B: String, gapPenalty: Int): Matrix[Int] = matrix[Int](
    A.length + 1,
    B.length + 1,
    0,
    (i: Int) => gapPenalty * i,
    (j: Int) => gapPenalty * j,
    (i: Int, j: Int, aboveleft: Int, left: Int, above: Int) =>
      Vector(aboveleft + S(A(i - 1), B(j - 1)), above + gapPenalty, left + gapPenalty).max
  )

  def alignStep(i: Int, j: Int, A: String, B: String, F: Matrix[Int], gapPenalty: Int): (Char, Char, Int, Int) =
    if (i > 0 && j > 0 && F(i, j) === F(i - 1, j - 1) + S(A(i - 1), B(j - 1))) {
      (A(i - 1), B(j - 1), i - 1, j - 1)
    } else if (i > 0 && F(i, j) === F(i - 1, j) + gapPenalty) {
      (A(i - 1), gap, i - 1, j)
    } else {
      assert(j > 0 && F(i, j) === F(i, j - 1) + gapPenalty)
      (gap, B(j - 1), i, j - 1)
    }

  def _optimalAlignment(i: Int, j: Int, A: String, B: String, gapPenalty: Int, F: Matrix[Int]): Stream[(Char, Char)] =
    if (i > 0 || j > 0) {
      val (preA, preB, newI, newJ) = alignStep(i, j, A, B, F, gapPenalty)
      cons((preA, preB), _optimalAlignment(newI, newJ, A, B, gapPenalty, F))
    } else {
      empty
    }

  def optimalAlignment(A: String, B: String, gapPenalty: Int = defaultGapPenalty): (String, String) = {
    val F = computeF(A, B, gapPenalty)
    val (alignmentA, alignmentB) = _optimalAlignment(A.length, B.length, A, B, gapPenalty, F).unzip
    (alignmentA.reverse.mkString(""), alignmentB.reverse.mkString(""))
  }

  def metricSpace(gapPenalty: Int = defaultGapPenalty): NeedlemanWunschMetricSpace = new NeedlemanWunschMetricSpace(gapPenalty)

  class NeedlemanWunschMetricSpace(gapPenalty: Int) extends MetricSpace[String, Int] {

    def distance(s1: String, s2: String): Int =
      computeF(s1, s2, gapPenalty)(s1.length, s2.length)

  }

}
