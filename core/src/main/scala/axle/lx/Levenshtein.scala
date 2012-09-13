package axle.lx

/**
 * Based on the Scala implementation of
 *
 * http://en.wikibooks.org/wiki/Algorithm_Implementation/Strings/Levenshtein_distance
 */

object Levenshtein extends EditDistance {

  import axle.matrix.JblasMatrixFactory._

  def distance(s1: String, s2: String): Int = {

    val lenStr1 = s1.length
    val lenStr2 = s2.length

    val d = zeros[Int](lenStr1 + 1, lenStr2 + 1)

    for (i <- 0 to lenStr1) d(i, 0) = i
    for (j <- 0 to lenStr2) d(0, j) = j

    for (i <- 1 to lenStr1; j <- 1 to lenStr2) {
      d(i, j) = min(d(i - 1, j) + 1, d(i, j - 1) + 1, d(i - 1, j - 1) + (if (s1(i - 1) == s2(j - 1)) 0 else 1))
    }

    d(lenStr1, lenStr2)
  }

  def min(nums: Int*): Int = nums.min

}