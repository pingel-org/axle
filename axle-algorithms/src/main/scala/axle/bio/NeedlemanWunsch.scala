package axle.bio

import scala.Stream.cons
import scala.Stream.empty
import scala.Vector

import axle.algebra.LinearAlgebra
import axle.syntax.linearalgebra._
import spire.algebra.MetricSpace
import spire.implicits.DoubleAlgebra
import spire.implicits.CharAlgebra
import spire.implicits.IntAlgebra
import spire.implicits.eqOps

/**
 *
 * http://en.wikipedia.org/wiki/Needleman-Wunsch_algorithm
 *
 */

object NeedlemanWunsch {

  /**
   * S is "similarity", computed by a fixed Int matrix
   *
   * S(a, b) === S(b, a)
   *
   */

  def S(x: Char, y: Char): Double = (x, y) match {
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
  val defaultGapPenalty = -5d

  def alignmentScore(A: String, B: String, gapPenalty: Double = defaultGapPenalty): Double = {
    assert(A.length === B.length)
    (0 until A.length).map(i =>
      if (A(i) === gap || B(i) === gap) {
        gapPenalty
      } else {
        S(A(i), B(i))
      }).sum
  }

  // ←
  // alignmentScore("AGACTAGTTAC", "CGA---GACGT")

  /**
   *
   * Computes the "F" matrix for two DNA sequences, A and B
   *
   */

  def computeF[M](A: String, B: String, gapPenalty: Double)(implicit la: LinearAlgebra[M, Int, Int, Double]): M =
    la.matrix(
      A.length + 1,
      B.length + 1,
      0,
      (i: Int) => gapPenalty * i,
      (j: Int) => gapPenalty * j,
      (i: Int, j: Int, aboveleft: Double, left: Double, above: Double) =>
        Vector(aboveleft + S(A(i - 1), B(j - 1)), above + gapPenalty, left + gapPenalty).max)

  def alignStep[M](i: Int, j: Int, A: String, B: String, F: M, gapPenalty: Double)(implicit la: LinearAlgebra[M, Int, Int, Double]): (Char, Char, Int, Int) =
    if (i > 0 && j > 0 && F.get(i, j) === F.get(i - 1, j - 1) + S(A(i - 1), B(j - 1))) {
      (A(i - 1), B(j - 1), i - 1, j - 1)
    } else if (i > 0 && F.get(i, j) === F.get(i - 1, j) + gapPenalty) {
      (A(i - 1), gap, i - 1, j)
    } else {
      assert(j > 0 && F.get(i, j) === F.get(i, j - 1) + gapPenalty)
      (gap, B(j - 1), i, j - 1)
    }

  def _optimalAlignment[M](i: Int, j: Int, A: String, B: String, gapPenalty: Double, F: M)(implicit la: LinearAlgebra[M, Int, Int, Double]): Stream[(Char, Char)] =
    if (i > 0 || j > 0) {
      val (preA, preB, newI, newJ) = alignStep(i, j, A, B, F, gapPenalty)
      cons((preA, preB), _optimalAlignment(newI, newJ, A, B, gapPenalty, F))
    } else {
      empty
    }

  def optimalAlignment[M](A: String, B: String, gapPenalty: Double = defaultGapPenalty)(implicit la: LinearAlgebra[M, Int, Int, Double]): (String, String) = {
    val F = computeF(A, B, gapPenalty)
    val (alignmentA, alignmentB) = _optimalAlignment(A.length, B.length, A, B, gapPenalty, F).unzip
    (alignmentA.reverse.mkString(""), alignmentB.reverse.mkString(""))
  }

  def metricSpace[M](gapPenalty: Double = defaultGapPenalty)(implicit la: LinearAlgebra[M, Int, Int, Double]) = NeedlemanWunschMetricSpace(gapPenalty)

  case class NeedlemanWunschMetricSpace[M](gapPenalty: Double)(implicit la: LinearAlgebra[M, Int, Int, Double])
    extends MetricSpace[String, Int] {

    def distance(s1: String, s2: String): Int =
      computeF(s1, s2, gapPenalty).get(s1.length, s2.length).toInt
  }

}
