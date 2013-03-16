package axle.bio

import math.max
import axle.matrix.JblasMatrixModule._

/**
 *
 * http://en.wikipedia.org/wiki/Needleman-Wunsch_algorithm
 *
 */

object NeedlemanWunsch {

  /**
   * S is "similarity", computed by a fixed Int matrix
   *
   * S(a, b) == S(b, a)
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

  def alignmentScore(A: String, B: String, gapPenalty: Int): Int = {
    assert(A.length == B.length)
    (0 until A.length).map(i =>
      if (A(i) == gap || B(i) == gap)
        -gapPenalty
      else
        S(A(i), B(i))
    ).sum
  }

  // ←
  // alignmentScore("AGACTAGTTAC", "CGA---GACGT")

  /**
   *
   * Computes the "F" matrix for two DNA sequences, A and B
   *
   */

  def computeF(A: String, B: String, gapPenalty: Int) = matrix[Int](
    A.length + 1,
    B.length + 1,
    0,
    (i: Int) => gapPenalty * i,
    (j: Int) => gapPenalty * j,
    (i: Int, j: Int, aboveleft: Int, left: Int, above: Int) =>
      Vector(aboveleft + S(A(i - 1), B(j - 1)), above + gapPenalty, left + gapPenalty).max
  )

  def optimalAlignment(A: String, B: String, gapPenalty: Int): (String, String) = {
    val F = computeF(A, B, gapPenalty)
    var alignmentA = List[Char]()
    var alignmentB = List[Char]()
    var i = A.length
    var j = B.length
    while (i > 0 || j > 0) {
      if (i > 0 && j > 0 && F(i, j) == F(i - 1, j - 1) + S(A(i - 1), B(j - 1))) {
        alignmentA = A(i - 1) :: alignmentA
        alignmentB = B(j - 1) :: alignmentB
        i = i - 1
        j = j - 1
      } else if (i > 0 && F(i, j) == F(i - 1, j) + gapPenalty) {
        alignmentA = A(i - 1) :: alignmentA
        alignmentB = gap :: alignmentB
        i = i - 1
      } else if (j > 0 && F(i, j) == F(i, j - 1) + gapPenalty) {
        alignmentA = gap :: alignmentA
        alignmentB = B(j - 1) :: alignmentB
        j = j - 1
      } else {
        println("no matching case")
      }
    }
    (alignmentA.mkString(""), alignmentB.mkString(""))
  }

}