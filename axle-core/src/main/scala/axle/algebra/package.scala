package axle

import scala.language.implicitConversions

import cats.implicits._
import cats.Order.catsKernelOrderingForOrder

import spire.algebra._
import spire.implicits.additiveGroupOps
import spire.implicits.additiveSemigroupOps

package object algebra {

  /**
   * dummy is not to be used widely, but is used for for scanLeft, where
   * it's often desirable to provide a throw-away value as the first argument
   * without using an Option type for an already complicated method signature.
   * 
   * A better work-around would be an alternate version of scanLeft that had
   * this behavior built in.
   *
   * Something like this:
   *
   * def scanLeftDropFirst[A, Repr, B, C, That](
   *   tl: scala.collection.TraversableLike[A, Repr])(z: C)(op: ((B, C), A) ⇒ (B, C))(
   *   implicit bf: scala.collection.generic.CanBuildFrom[Repr, (B, C), That]) =
   *   tl.scanLeft((axle.dummy[B], z))(op).drop(1)
   */

  def dummy[T]: T = null.asInstanceOf[T]

  def ignore[T]: T => Unit = (t: T) => {}

  def id[A](x: A): A = x

  // a.k.a. `…`
  def etc[N](n: N)(implicit ringN: Ring[N]): Iterable[N] = new Iterable[N] {

    def iterator: Iterator[N] = new Iterator[N] {

      var current = n

      def next(): N = {
        val rc = current
        current = ringN.plus(current, ringN.one)
        rc
      }

      def hasNext: Boolean = true
    }
  }

  def bytewise(left: Array[Byte])(right: Array[Byte])(op: (Byte, Byte) => Byte): Array[Byte] =
    left.zip(right).map(lr => (op(lr._1, lr._2))).toArray

  def ⊕(left: Array[Byte])(right: Array[Byte]): Array[Byte] =
    bytewise(left)(right)({ case (l, r) => (l ^ r).toByte })

  def tensorProduct[T](xs: Vector[T], ys: Vector[T])(implicit multT: MultiplicativeSemigroup[T]): Vector[T] = 
    for {
      x <- xs
      y <- ys
    } yield multT.times(x, y)

  implicit def catsifyAdditiveGroup[T](ag: _root_.algebra.ring.AdditiveGroup[T]): cats.kernel.Group[T] =
    new cats.kernel.Group[T] {
      def inverse(a: T): T = ag.negate(a)
      def empty: T = ag.zero
      def combine(x: T, y: T): T = ag.plus(x, y)
    }

  // Function application patterns

  def applyK[N](f: N => N, x0: N, k: Int): N =
    (1 to k).foldLeft(x0)({ case (x, _) => f(x) })

  // Iterator methods

  def applyForever[N](f: N => N, x0: N): Iterator[N] =
    Iterator
      .continually(())
      .scanLeft(x0)({ case (x, _) => f(x) })

  def trace[N](f: N => N, x0: N): Iterator[(N, Set[N])] =
    Iterator
      .continually(())
      .scanLeft((x0, Set.empty[N]))({
        case ((x, points), _) =>
          (f(x), points + x)
      })

  /**
   * mergeStreams takes streams that are ordered w.r.t. Order[T]
   *
   */

  def mergeStreams[T](streams: Seq[LazyList[T]])(
    implicit
    orderT: Order[T]): LazyList[T] = {

    val frontier = streams.flatMap(_.headOption)

    if (frontier.size === 0) {
      LazyList.empty
    } else {
      val head = frontier.min
      LazyList.cons(head, mergeStreams(streams.map(_.dropWhile(_ === head))))
    }
  }

  def filterOut[T](stream: LazyList[T], toRemove: LazyList[T])(implicit orderT: Order[T]): LazyList[T] =
    if (stream.isEmpty || toRemove.isEmpty) {
      stream
    } else {
      val remove = toRemove.head
      stream.takeWhile(_ < remove) ++ filterOut(stream.dropWhile(_ <= remove), toRemove.drop(1))
    }

  def lazyListsFrom[N](n: N)(implicit orderN: Order[N], ringN: Ring[N]): LazyList[N] =
    LazyList.cons(n, lazyListsFrom(ringN.plus(n, ringN.one)))


  /**
   * gaps
   *
   * assumes that the input xs are already sorted
   */

  def gaps[T](xs: Seq[T])(implicit ringT: Ring[T]): Seq[(T, T)] = {
    import ringT.one
    xs.zip(xs.drop(1))
      .map({ case (x, y) => (x, y - x) })
      .filterNot(_._2 == one)
      .map({ case (x, g) => (x + one, x + g - one) })
  }

  def runs[T](xs: Seq[T], breaks: Set[T]): Seq[(T, T)] =
    if (xs.isEmpty) {
      List.empty
    } else {
      val tail = xs.drop(1)
      val run = tail.takeWhile(x => !breaks.contains(x))
      (xs.head, (xs.head +: run).last) +: runs(tail.drop(run.length), breaks)
    }

  /**
   * runs
   *
   * assumes xs is already sorted
   */

  def runs[T](xs: Seq[T])(implicit ringT: Ring[T], orderT: Order[T]): Seq[(T, T)] = {
    import ringT.one
    val breaks = xs.zip(xs.drop(1)).filter({ case (x, y) => orderT.compare(y - x, one) == 1 }).map(_._2).toSet
    runs(xs, breaks)
  }

  // List methods

  def replicate[T](n: Int)(v: T): List[T] = (0 until n).map(i => v).toList

  def reverse[T](l: List[T]): List[T] = l.reverse

  def intersperse[T](d: T)(l: List[T]): List[T] =
    (0 until (2 * l.size - 1)).map(i => i % 2 match { case 0 => l(i / 2) case 1 => d }).toList

}
