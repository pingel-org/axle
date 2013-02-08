package axle

import collection._
import Stream.{ empty, cons }

/**
 * Based on Python's itertools.permutations function
 *
 * http://docs.python.org/library/itertools.html#itertools.permutations
 *
 * Permutations("ABCD".toIndexedSeq, 2)
 * Permutations(0 until 3)
 *
 */

object Permutations {

  def apply[E](pool: IndexedSeq[E], r: Int): Permutations[E] = new Permutations[E](pool, r)
}

class Permutations[E](pool: IndexedSeq[E], r: Int) extends Iterable[List[E]] {

  val n = pool.length

  override def size() = if (r >= 0 && r <= n) (n.factorial / (n - r).factorial) else 0

  def loop2branchTrue(
    indices0: IndexedSeq[Int],
    cycles0: IndexedSeq[Int],
    i0: Int): (IndexedSeq[Int], IndexedSeq[Int]) =
    (indices0(0 until i0) ++ indices0(i0 + 1 until n) ++ indices0(i0 until i0 + 1),
      cycles0.updated(i0, n - i0)
    )

  def loop2branchFalse(
    indices0: IndexedSeq[Int],
    cycles0: IndexedSeq[Int],
    i0: Int): (List[E], IndexedSeq[Int]) = {
    val indices1 = indices0.swap(indices0((n - cycles0(i0)) % n), indices0(i0))
    ((0 until r).map(indices1(_)).map(pool(_)).toList, indices1)
  }

  def loop2branch(
    indices0: IndexedSeq[Int],
    cycles0: IndexedSeq[Int],
    i0: Int): (Option[List[E]], IndexedSeq[Int], IndexedSeq[Int], Int, Boolean) =
    if (cycles0(i0) == 0) {
      val (indices1, cycles1) = loop2branchTrue(indices0, cycles0, i0)
      (None, indices1, cycles1, i0 - 1, false)
    } else {
      val (result2, indices2) = loop2branchFalse(indices0, cycles0, i0)
      (Some(result2), indices2, cycles0, i0, true)
    }

  // @tailrec
  def loop2(
    indices0: IndexedSeq[Int],
    cycles0: IndexedSeq[Int],
    i0: Int,
    broken0: Boolean): (Stream[List[E]], IndexedSeq[Int], IndexedSeq[Int], Boolean) =
    if (i0 >= 0 && !broken0) {
      val cycles1 = cycles0.updated(i0, cycles0(i0) - 1)
      val (result, indices2, cycles2, i2, broken2) = loop2branch(indices0, cycles1, i0)
      val (subStream, indices3, cycles3, broken3) = loop2(indices2, cycles2, i2, broken2)
      (if (result.isDefined) cons(result.get, subStream) else subStream, indices3, cycles3, broken3)
    } else {
      (empty, indices0, cycles0, broken0) // Is broken0 the right version of broken?
    }

  // @tailrec
  def loop1(indices: IndexedSeq[Int], cycles: IndexedSeq[Int]): Stream[List[E]] = {
    val (subStream, indicesOut, cyclesOut, broken) = loop2(indices, cycles, r - 1, false)
    subStream ++ (if (broken) loop1(indicesOut, cyclesOut) else empty)
  }

  lazy val result: Stream[List[E]] = if (r <= n && n > 0) {
    val indices = (0 until n)
    val head = (0 until r).map(indices(_)).map(pool(_)).toList
    cons(head, loop1(indices, n.until(n - r, -1)))
  } else {
    empty
  }

  def iterator() = result.iterator

}
