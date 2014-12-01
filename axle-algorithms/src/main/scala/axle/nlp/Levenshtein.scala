package axle.nlp

import axle.algebra.Matrix
import spire.algebra.MetricSpace
import spire.implicits.CharAlgebra
import spire.implicits.eqOps
import axle.syntax.matrix._

/**
 * Based on the Scala implementation of
 *
 * http://en.wikibooks.org/wiki/Algorithm_Implementation/Strings/Levenshtein_distance
 */

case class Levenshtein[M[_]]()(implicit ev: Matrix[M]) extends MetricSpace[String, Int] {

  def distance(s1: String, s2: String): Int = {

    val lenStr1 = s1.length
    val lenStr2 = s2.length

    val d = ev.matrix(
      lenStr1 + 1,
      lenStr2 + 1,
      0,
      (r: Int) => r,
      (c: Int) => c,
      (r: Int, c: Int, diag: Int, left: Int, top: Int) => min(left + 1, top + 1, diag + (if (s1(r - 1) === s2(c - 1)) 0 else 1)))

    d.get(lenStr1, lenStr2)
  }

  def min(nums: Int*): Int = nums.min

}
