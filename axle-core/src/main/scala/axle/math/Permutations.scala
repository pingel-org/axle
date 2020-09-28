package axle.math

import scala.reflect.ClassTag

import cats.implicits._

import spire.algebra._

import axle.math.factorial
import axle.syntax.indexed._
import axle.algebra.Indexed._

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
  val untilN = (0 until n).toVector
  val untilR = (0 until r).toVector

  implicit val intRing: Ring[Int] = spire.implicits.IntAlgebra

  override def size: Int = if (r >= 0 && r <= n) ( factorial(n) / factorial(n - r) ) else 0

  private[this] def loop2branchTrue(
    indices0: Vector[Int],
    cycles0:  Vector[Int],
    i0:       Int): (Vector[Int], Vector[Int]) = {
    (indices0.slyce(0 until i0) ++ indices0.slyce(i0 + 1 until n) ++ indices0.slyce(i0 until i0 + 1),
      cycles0.updated(i0, n - i0))
  }

  private[this] def loop2branchFalse(
    indices0: Vector[Int],
    cycles0:  Vector[Int],
    i0:       Int): (Vector[E], Vector[Int]) = {
    val indices1 = indices0.swap(indices0((n - cycles0(i0)) % n), indices0(i0))
    (untilR.map(indices1).map(pool), indices1)
  }

  private[this] def loop2branch(
    indices0: Vector[Int],
    cycles0:  Vector[Int],
    i0:       Int): (Option[IndexedSeq[E]], Vector[Int], Vector[Int], Int, Boolean) =
    if (cycles0(i0) === 0) {
      val (indices1, cycles1) = loop2branchTrue(indices0, cycles0, i0)
      (None, indices1, cycles1, i0 - 1, false)
    } else {
      val (result2, indices2) = loop2branchFalse(indices0, cycles0, i0)
      (Some(result2.toIndexedSeq), indices2, cycles0, i0, true)
    }

  private[this] def loop2(
    indices0: Vector[Int],
    cycles0:  Vector[Int],
    i0:       Int,
    broken0:  Boolean): (LazyList[IndexedSeq[E]], Vector[Int], Vector[Int], Boolean) =
    if (i0 >= 0 && !broken0) {
      val cycles1 = cycles0.updated(i0, cycles0(i0) - 1)
      val (result, indices2, cycles2, i2, broken2) = loop2branch(indices0, cycles1, i0)
      val (subStream, indices3, cycles3, broken3) = loop2(indices2, cycles2, i2, broken2)
      (if (result.isDefined) LazyList.cons(result.get, subStream) else subStream, indices3, cycles3, broken3)
    } else {
      (LazyList.empty, indices0, cycles0, broken0)
    }

  private[this] def loop1(indices: Vector[Int], cycles: Vector[Int]): LazyList[IndexedSeq[E]] = {
    val (subStream, indicesOut, cyclesOut, broken) = loop2(indices, cycles, r - 1, false)
    subStream ++ (if (broken) loop1(indicesOut, cyclesOut) else empty)
  }

  lazy val result: LazyList[IndexedSeq[E]] = if (r <= n && n > 0) {
    val indices = untilN
    val head = untilR.map(indices(_)).map(pool(_))
    LazyList.cons(head.toIndexedSeq, loop1(indices, n.until(n - r, -1).toVector))
  } else {
    LazyList.empty
  }

  def iterator: Iterator[IndexedSeq[E]] = result.iterator

}
