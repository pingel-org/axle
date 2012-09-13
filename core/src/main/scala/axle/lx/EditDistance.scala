package axle.lx

import axle._

/**
 * Edit Distance
 *
 */

trait EditDistance {

  def distance(s1: String, s2: String): Int

  /**
   * triangleInequalityHolds
   *
   * Applies the Triangle Inequality using all the triples formed by the
   * given data to see if this is a true "distance"
   *
   * http://en.wikipedia.org/wiki/Triangle_inequality
   */

  def triangleInequalityHolds(data: IndexedSeq[String]): Boolean =
    data.triples.∀({
      case (a, b, c) =>
        distance(a, b) + distance(b, c) >= distance(a, c)
    })
}
