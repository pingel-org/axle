package axle

import scala.reflect.ClassTag
import scala.Stream.cons
import scala.Stream.empty

import cats.implicits._

/**
 * Based on Python's itertools.permutations function
 *
 * http://docs.python.org/library/itertools.html#itertools.permutations
 *
 * Permutations("ABCD".toIndexedSeq, 2)
 * Permutations(0 until 3)
 *
 */

case class Permutations[E: ClassTag](pool: IndexedSeq[E], r: Int)
  extends Iterable[IndexedSeq[E]] {

  val n = pool.length
  val untilN = (0 until n).toArray
  val untilR = (0 until r).toArray

  override def size: Int = if (r >= 0 && r <= n) (n.factorial / (n - r).factorial) else 0

  private[this] def loop2branchTrue(
    indices0: Array[Int],
    cycles0:  Array[Int],
    i0:       Int): (Array[Int], Array[Int]) = {
    (indices0(0 until i0) ++ indices0(i0 + 1 until n) ++ indices0(i0 until i0 + 1),
      cycles0.updated(i0, n - i0))
  }

  private[this] def loop2branchFalse(
    indices0: Array[Int],
    cycles0:  Array[Int],
    i0:       Int): (Array[E], Array[Int]) = {
    val indices1 = indices0.swap(indices0((n - cycles0(i0)) % n), indices0(i0))
    (untilR.map(indices1).map(pool).toArray, indices1)
  }

  private[this] def loop2branch(
    indices0: Array[Int],
    cycles0:  Array[Int],
    i0:       Int): (Option[IndexedSeq[E]], Array[Int], Array[Int], Int, Boolean) =
    if (cycles0(i0) === 0) {
      val (indices1, cycles1) = loop2branchTrue(indices0, cycles0, i0)
      (None, indices1, cycles1, i0 - 1, false)
    } else {
      val (result2, indices2) = loop2branchFalse(indices0, cycles0, i0)
      (Some(result2), indices2, cycles0, i0, true)
    }

  private[this] def loop2(
    indices0: Array[Int],
    cycles0:  Array[Int],
    i0:       Int,
    broken0:  Boolean): (Stream[IndexedSeq[E]], Array[Int], Array[Int], Boolean) =
    if (i0 >= 0 && !broken0) {
      val cycles1 = cycles0.updated(i0, cycles0(i0) - 1)
      val (result, indices2, cycles2, i2, broken2) = loop2branch(indices0, cycles1, i0)
      val (subStream, indices3, cycles3, broken3) = loop2(indices2, cycles2, i2, broken2)
      (if (result.isDefined) cons(result.get, subStream) else subStream, indices3, cycles3, broken3)
    } else {
      (empty, indices0, cycles0, broken0)
    }

  private[this] def loop1(indices: Array[Int], cycles: Array[Int]): Stream[IndexedSeq[E]] = {
    val (subStream, indicesOut, cyclesOut, broken) = loop2(indices, cycles, r - 1, false)
    subStream ++ (if (broken) loop1(indicesOut, cyclesOut) else empty)
  }

  lazy val result: Stream[IndexedSeq[E]] = if (r <= n && n > 0) {
    val indices = untilN
    val head = untilR.map(indices(_)).map(pool(_))
    cons(head, loop1(indices, n.until(n - r, -1).toArray))
  } else {
    empty
  }

  def iterator: Iterator[IndexedSeq[E]] = result.iterator

}
