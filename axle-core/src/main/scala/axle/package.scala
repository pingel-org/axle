import scala.collection.mutable.Buffer


import scala.language.implicitConversions
import cats.Show
import cats.kernel.Eq
import cats.kernel.Order
import cats.Order.catsKernelOrderingForOrder
import cats.implicits._
import spire.algebra._
import spire.implicits.additiveGroupOps
import spire.math.Rational

/**
 *
 */

package object axle {

  def showDoubleWithPrecision(p: Int = 6): Show[Double] =
    new Show[Double] {
      val fmt = s"""%.${p}f"""
      def show(d: Double): String = fmt.format(d)
    }

  implicit val showNode: Show[xml.Node] = Show.fromToString[xml.Node]

  implicit val showRational: Show[Rational] = Show.fromToString[Rational]

  def ignore[T]: T => Unit = (t: T) => {}

  def prefixedDisplay(prefix: String)(display: String => Unit): String => Unit =
    (s: String) => s.split("\n").foreach(line => display(prefix + "> " + line))

  /**
   * gaps
   *
   * assumes that the input xs are already sorted
   */
  def gaps[T](xs: Seq[T])(implicit ringT: Ring[T]): Seq[(T, T)] = {
    import ringT.one
    import spire.implicits._
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
    import spire.implicits._
    val breaks = xs.zip(xs.drop(1)).filter({ case (x, y) => orderT.compare(y - x, one) == 1 }).map(_._2).toSet
    runs(xs, breaks)
  }

  //  val Sigma = Σ _
  //
  //  val Pi = Π _

  val ∀ = forall

  val ∃ = thereexists

  implicit def enrichGenSeq[T](genSeq: collection.GenSeq[T]): EnrichedGenSeq[T] = EnrichedGenSeq(genSeq)

  implicit def enrichGenTraversable[T: Manifest](gt: collection.GenTraversable[T]): EnrichedGenTraversable[T] = EnrichedGenTraversable(gt)

  implicit def enrichIndexedSeq[T: Manifest](is: IndexedSeq[T]): EnrichedIndexedSeq[T] = EnrichedIndexedSeq(is)

  implicit def enrichIterator[T](it: Iterator[T]) = new EnrichedIterator(it)

  implicit def enrichByteArray(barr: Array[Byte]): EnrichedByteArray = EnrichedByteArray(barr)

  implicit def enrichMutableBuffer[T](buffer: Buffer[T]): EnrichedMutableBuffer[T] = EnrichedMutableBuffer(buffer)

  implicit def enrichArray[T: Manifest](arr: Array[T]): EnrichedArray[T] = EnrichedArray(arr)

  implicit def enrichInt(n: Int): EnrichedInt = EnrichedInt(n)

  def applyK[N](f: N => N, x0: N, k: Int): N =
    (1 to k).foldLeft(x0)({ case (x, _) => f(x) })

  def applyForever[N](f: N => N, x0: N): Iterator[N] =
    Iterator
      .continually(Unit)
      .scanLeft(x0)({ case (x, _) => f(x) })

  def trace[N](f: N => N, x0: N): Iterator[(N, Set[N])] = {
    Iterator
      .continually(Unit)
      .scanLeft((x0, Set.empty[N]))({
        case ((x, points), _) =>
          (f(x), points + x)
      })
  }

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

  def mergeStreams[T](streams: Seq[Stream[T]])(
    implicit eqT: Eq[T],
    orderT: Order[T]): Stream[T] = {

    val frontier = streams.flatMap(_.headOption)

    if (frontier.size === 0) {
      Stream.empty
    } else {
      val head = frontier.min
      Stream.cons(head, mergeStreams(streams.map(_.dropWhile(_ === head))))
    }
  }

  def filterOut[T](stream: Stream[T], toRemove: Stream[T])(implicit orderT: Order[T]): Stream[T] =
    if (stream.isEmpty || toRemove.isEmpty) {
      stream
    } else {
      val remove = toRemove.head
      stream.takeWhile(_ < remove) append filterOut(stream.dropWhile(_ <= remove), toRemove.drop(1))
    }

  def streamFrom[N](n: N)(implicit orderN: Order[N], ringN: Ring[N]): Stream[N] =
    Stream.cons(n, streamFrom(ringN.plus(n, ringN.one)))

  // Fundamental:

  def id[A](x: A): A = x

  // def argmax[K, N: Order](ks: Iterable[K], f: K => N): K = ks.map(k => (k, f(k))).maxBy(_._2)._1

  // IO

  // TODO echo characters as typed (shouldn't have to use jline for this)
  def getLine(): String = scala.io.StdIn.readLine()

  // List enrichments:

  def replicate[T](n: Int)(v: T): List[T] = (0 until n).map(i => v).toList

  def reverse[T](l: List[T]): List[T] = l.reverse

  def intersperse[T](d: T)(l: List[T]): List[T] =
    (0 until (2 * l.size - 1)).map(i => i % 2 match { case 0 => l(i / 2) case 1 => d }).toList

  def string[T: Show](t: T): String = Show[T].show(t)

  def show[T: Show](t: T): String = Show[T].show(t)

  def print[T: Show](t: T): Unit = println(string(t))

  def html[T: HtmlFrom](t: T): xml.Node = HtmlFrom[T].toHtml(t)

}
