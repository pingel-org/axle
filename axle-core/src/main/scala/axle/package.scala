import scala.collection.mutable.Buffer

import scala.reflect.ClassTag
import scala.collection.immutable.TreeMap
import scala.language.implicitConversions

import cats.Show
import cats.implicits._

import spire.algebra._
import spire.random.Generator
import spire.implicits.additiveSemigroupOps
import spire.implicits.additiveGroupOps

/**
 *
 */

package object axle {

  // unicode aliases

  //  val Sigma = Σ _
  //
  //  val Pi = Π _

  val ∀ = forall

  val ∃ = thereexists

  def showDoubleWithPrecision(p: Int = 6): Show[Double] = d => {
    val fmt = s"""%.${p}f"""
    fmt.format(d)
  }

  // missing Eq witnesses

  implicit def eqIterable[T](implicit eqT: Eq[T]): Eq[Iterable[T]] =
    (x, y) =>
      x.size === y.size && x.zip(y).forall({ case (p, q) => eqT.eqv(p, q) })

  implicit def eqTreeMap[K, V](implicit eqK: Eq[K], eqV: Eq[V]): Eq[TreeMap[K, V]] =
    (x, y) =>
      x.keys === y.keys && x.keySet.forall(k => x.get(k) === y.get(k))

  // basic functions

  /**
   * dummy is not to be used widely, but is used for for scanLeft, where
   * it's often desirable to provide a throw-away value as the first argument
   * without using an Option type for an already complicated method signature.
   * A better work-around would be an alternate version of scanLeft that had
   * this behavior built in.
   *
   * Something like this:
   *
   * def scanLeftDropFirst[A, Repr, B, C, That](
   *   tl: scala.collection.TraversableLike[A, Repr])(z: C)(op: ((B, C), A) ⇒ (B, C))(
   *   implicit bf: scala.collection.generic.CanBuildFrom[Repr, (B, C), That]) =
   *   tl.scanLeft((axle.dummy[B], z))(op) // Caller should .drop(1). TODO do that here
   */

  def dummy[T]: T = null.asInstanceOf[T]

  def ignore[T]: T => Unit = (t: T) => {}

  def id[A](x: A): A = x

  // Seq operations

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

  /**
   *
   * shuffle
   */

  def shuffle[T](xs: List[T])(gen: Generator): List[T] =
    xs.map(x => (x, gen.nextInt())).sortBy(_._2).map(_._1)

  // List methods

  def replicate[T](n: Int)(v: T): List[T] = (0 until n).map(i => v).toList

  def reverse[T](l: List[T]): List[T] = l.reverse

  def intersperse[T](d: T)(l: List[T]): List[T] =
    (0 until (2 * l.size - 1)).map(i => i % 2 match { case 0 => l(i / 2) case 1 => d }).toList

  // Axle enrichments of scala collections

  implicit def enrichIterable[T](ita: Iterable[T]): EnrichedIterable[T] = EnrichedIterable(ita)

  implicit def enrichIndexedSeq[T](is: IndexedSeq[T]): EnrichedIndexedSeq[T] = EnrichedIndexedSeq(is)

  implicit def enrichIterator[T](it: Iterator[T]) = new EnrichedIterator(it)

  implicit def enrichByteArray(barr: Array[Byte]): EnrichedByteArray = EnrichedByteArray(barr)

  implicit def enrichMutableBuffer[T](buffer: Buffer[T]): EnrichedMutableBuffer[T] = EnrichedMutableBuffer(buffer)

  implicit def enrichArray[T: ClassTag](arr: Array[T]): EnrichedArray[T] = EnrichedArray(arr)

  implicit def enrichInt(n: Int): EnrichedInt = EnrichedInt(n)

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

  def orbit[N](f: N => N, x0: N, close: N => N => Boolean): List[N] =
    trace(f, x0)
      .takeWhile({
        case (x, points) =>
          // TODO inefficient. query points for the closest (or bounding) elements to x
          !points.exists(close(x))
      })
      .lastOption.toList
      .flatMap(_._2.toList)

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

  // Typeclass-based method invocations

  def print[T: Show](t: T): Unit = println(t.show)

}
