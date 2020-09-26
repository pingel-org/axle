package axle

import scala.reflect.ClassTag
import scala.annotation.tailrec

import cats.implicits._
import spire.algebra._
import axle.math.factorial

case class Combinations[E: ClassTag](pool: IndexedSeq[E], r: Int) extends Iterable[IndexedSeq[E]] {

  val n = pool.size

  if (r > n) {
    throw new IndexOutOfBoundsException()
  }

  implicit val intRing: Ring[Int] = spire.implicits.IntAlgebra

  lazy val _size = if (0 <= r && r <= n) (factorial(n) / factorial(r) / factorial(n - r)) else 0

  override def size: Int = _size

  @tailrec
  private[this] def loop3(indices0: Array[Int], i0: Int, broken0: Boolean): (Boolean, Int) =
    if (i0 >= 0 && !broken0) {
      val broken1 = (indices0(i0) != (i0 + n - r))
      loop3(indices0, if (broken1) i0 else (i0 - 1), broken1)
    } else {
      (broken0, i0)
    }

  def loop2(indices0: Array[Int]): (LazyList[IndexedSeq[E]], Array[Int], Boolean) = {
    val (broken1, i0) = loop3(indices0, r - 1, false)
    if (!broken1) {
      (LazyList.empty, indices0, true)
    } else {
      val indices1 = indices0.zipWithIndex.map({
        case (v, j) =>
          if (j === i0) {
            v + 1
          } else if (j >= (i0 + 1) && j < r) {
            (indices0(j - 1) + 1)
          } else {
            v
          }
      })
      val head = indices1.map(pool)
      val (tail, indices2, done) = loop2(indices1)
      (LazyList.cons(head.toIndexedSeq, tail), indices2, done)
    }
  }

  def loop1(indices0: Array[Int], done0: Boolean): LazyList[IndexedSeq[E]] =
    if (done0) {
      LazyList.empty
    } else {
      val (subStream, indices1, done1) = loop2(indices0)
      subStream ++ loop1(indices1, done1)
    }

  lazy val result: LazyList[IndexedSeq[E]] = {
    val indices = (0 until r)
    LazyList.cons(indices.map(pool), loop1(indices.toArray, false))
  }

  def iterator: Iterator[IndexedSeq[E]] = result.iterator

}
