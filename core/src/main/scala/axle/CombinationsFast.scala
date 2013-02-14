package axle

/**
 * Based on Python's itertools.permutations function
 *
 * http://docs.python.org/library/itertools.html#itertools.combinations
 *
 * CombionationsFast("ABCD".toIndexedSeq, 2)
 * CombionationsFast(0 until 4, 3)
 */

import collection._

object CombinationsFast {

  def apply[E](pool: IndexedSeq[E], r: Int): CombinationsFast[E] = new CombinationsFast[E](pool, r)
}

class CombinationsFast[E](pool: IndexedSeq[E], r: Int) extends Iterable[List[E]] {

  val n = pool.size

  if (r > n) {
    throw new IndexOutOfBoundsException()
  }

  lazy val syze = if (0 <= r && r <= n) { n.factorial / r.factorial / (n - r).factorial } else { 0 }

  override def size(): Int = syze

  val yeeld = new mutable.ListBuffer[List[E]]() // TODO substitute for "yield" for now

  def iterator() = yeeld.iterator

  val indices = (0 until r).toBuffer
  yeeld += indices.map(pool(_)).toList
  var done = false

  while (!done) {
    var broken = false
    var i = r - 1
    while (i >= 0 && !broken) {
      if (indices(i) != (i + n - r)) {
        broken = true
      }
      if (!broken) {
        i -= 1
      }
    }
    if (!broken) {
      done = true
    } else {
      indices(i) += 1
      for (j <- (i + 1 until r)) {
        indices(j) = indices(j - 1) + 1
      }
      yeeld += indices.map(pool(_)).toList
    }
  }

}
