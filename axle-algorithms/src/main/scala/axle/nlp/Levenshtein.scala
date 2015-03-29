package axle.nlp

import axle.algebra.LinearAlgebra
import spire.algebra.MetricSpace
import spire.implicits.CharAlgebra
import spire.implicits.eqOps
import axle.syntax.linearalgebra._

/**
 * Based on the Scala implementation of
 *
 * http://en.wikibooks.org/wiki/Algorithm_Implementation/Strings/Levenshtein_distance
 */

case class Levenshtein[M]()(implicit la: LinearAlgebra[M, Int, Int, Double])
  extends MetricSpace[String, Int] {

  def distance(s1: String, s2: String): Int = {

    val lenStr1 = s1.length
    val lenStr2 = s2.length

    val d = la.matrix(
      lenStr1 + 1,
      lenStr2 + 1,
      0,
      (r: Int) => r,
      (c: Int) => c,
      (r: Int, c: Int, diag: Double, left: Double, top: Double) => min(left + 1, top + 1, diag + (if (s1(r - 1) === s2(c - 1)) 0d else 1d)))

    d.get(lenStr1, lenStr2).toInt
  }

  def min(nums: Double*): Double = nums.min

}
