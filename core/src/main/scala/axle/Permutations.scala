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

  override def size() = if (r >= 0 && r <= n) { n.factorial / (n - r).factorial } else { 0 }

  def loop2branchTrue(
    indices: IndexedSeq[Int],
    cycles: IndexedSeq[Int],
    i: Int): (IndexedSeq[Int], IndexedSeq[Int]) = {
    //indices(i until n) = indices(i + 1 until n) ++ indices(i until i + 1)
    //cycles(i) = n - i
    (indices(0 until i) ++ indices(i + 1 until n) ++ indices(i until i + 1),
      cycles.updated(i, n - i)
    )
  }

  def loop2branchFalse(
    indices: IndexedSeq[Int],
    cycles: IndexedSeq[Int],
    i: Int): (List[E], IndexedSeq[Int]) = {
    // val j = cycles(i)
    val indicesOut = indices.swap(indices((n - cycles(i)) % n), indices(i))
    ((0 until r).map(indicesOut(_)).map(pool(_)).toList, indicesOut)
  }

  def loop2branch(
    indices: IndexedSeq[Int],
    cycles: IndexedSeq[Int],
    i: Int): (Option[List[E]], IndexedSeq[Int], IndexedSeq[Int], Int, Boolean) = {
    if (cycles(i) == 0) {
      val (indicesOut, cyclesOut) = loop2branchTrue(indices, cycles, i)
      (None, indicesOut, cyclesOut, i - 1, false)
    } else {
      val (resultOut, indicesOut) = loop2branchFalse(indices, cycles, i)
      (Some(resultOut), indicesOut, cycles, i, true)
    }
  }

  // @tailrec
  def loop2(
    indices0: IndexedSeq[Int],
    cycles0: IndexedSeq[Int],
    i0: Int,
    broken0: Boolean): (Stream[List[E]], IndexedSeq[Int], IndexedSeq[Int], Boolean) = {
    if (i0 >= 0 && !broken0) {
      val cycles1 = cycles0.updated(i0, cycles0(i0) - 1)
      val (result, indices2, cycles2, i2, broken2) = loop2branch(indices0, cycles1, i0)
      val (subStream, indices3, cycles3, broken3) = loop2(indices2, cycles2, i2, broken2)
      (cons(result.get, subStream), indices3, cycles3, broken3)
    } else {
      (empty, indices0, cycles0, broken0) // Is broken0 the right version of broken?
    }
  }

  
  // @tailrec
  def loop1(indices: IndexedSeq[Int], cycles: IndexedSeq[Int]): Stream[List[E]] = {
    val (subStream, indicesOut, cyclesOut, broken) = loop2(indices, cycles, r - 1, false)
    subStream ++ (if (!broken) loop1(indicesOut, cyclesOut) else empty)
  }

  val result: Stream[List[E]] = if (r <= n && n > 0) {
    val indices = (0 until n)
    cons((0 until r).map(indices(_)).map(pool(_)).toList, loop1(indices, n.until(n - r, -1)))
  } else {
    empty
  }

  def iterator() = result.iterator

}
