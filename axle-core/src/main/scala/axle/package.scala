
/**
 * Copyright (c) 2011-2014 Adam Pingel
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 3. The name of the author may not be used to endorse or promote products
 *    derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 */

import scala.collection.mutable.Buffer

import axle.algebra.Aggregatable
import axle.algebra.Finite
import axle.algebra.Functor
import axle.algebra.Π
import cats.Show
import cats.kernel.Eq
import cats.implicits._
import spire.algebra.Field
import spire.algebra.NRoot
import cats.kernel.Order
import spire.algebra.Module
import spire.algebra.MultiplicativeMonoid
import spire.algebra.Rng
import spire.algebra.Ring
import spire.algebra.Trig
import spire.implicits.moduleOps
import spire.implicits.nrootOps
import spire.implicits.semiringOps
import spire.implicits.multiplicativeSemigroupOps
import spire.implicits.additiveGroupOps
import spire.math.Rational
// import spire.math.Real
import axle.quanta.Angle
import axle.quanta.UnittedQuantity
import axle.quanta.AngleConverter
import axle.quanta.Distance
import spire.math.ConvertableTo
import scala.language.implicitConversions

/**
 *
 * See spire.optional.unicode.SymbolicSetOps for ∩ ∪ etc
 *
 */

package object axle {

  def showDoubleWithPrecision(p: Int = 6): Show[Double] =
    new Show[Double] {
      val fmt = s"""%.${p}f"""
      def show(d: Double): String = fmt.format(d)
    }

  implicit val showSymbol: Show[Symbol] = Show.fromToString[Symbol]

  implicit val eqSymbol: Eq[Symbol] = Eq.fromUniversalEquals[Symbol]

  implicit val showBD: Show[BigDecimal] = Show.fromToString[BigDecimal]

  implicit val showNode: Show[xml.Node] = Show.fromToString[xml.Node]

  implicit val showRational: Show[Rational] = Show.fromToString[Rational]

  implicit val orderSymbol: Order[Symbol] =
    Order.from((x: Symbol, y: Symbol) => Order[String].compare(string(x), string(y)))

  def dropOutput(s: String): Unit = {}

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

  /**
   * Englishman John Wallis (1616 - 1703) approximation of π in 1655
   *
   */
  def wallisΠ(iterations: Int = 10000) =
    2 * Π[Rational, IndexedSeq[Rational]]((1 to iterations) map { n => Rational((2 * n) * (2 * n), (2 * n - 1) * (2 * n + 1)) })

  /**
   * Monte Carlo approximation of pi http://en.wikipedia.org/wiki/Monte_Carlo_method
   *
   * TODO get n2v implicitly?
   *
   */

  def monteCarloPiEstimate[F, N, V: ConvertableTo, G](
    trials: F,
    n2v: N => V)(
      implicit finite: Finite[F, N],
      functor: Functor[F, N, V, G],
      agg: Aggregatable[G, V, V],
      field: Field[V]): V = {

    import spire.math.random
    import axle.algebra.Σ
    import axle.syntax.functor.functorOps
    import spire.implicits.multiplicativeSemigroupOps
    import spire.implicits.multiplicativeGroupOps

    val randomPointInCircle: () => V = () => {
      val x = random * 2 - 1
      val y = random * 2 - 1
      if (x * x + y * y < 1) field.one else field.zero
    }

    val vFour = ConvertableTo[V].fromDouble(4d)

    val counts: G = trials.map(i => randomPointInCircle())

    val s: V = Σ(counts)

    val numerator: V = vFour * s

    val denominator: V = n2v(finite.size(trials))

    numerator / denominator
  }

  def distanceOnSphere[N: MultiplicativeMonoid](
    angle: UnittedQuantity[Angle, N],
    sphereRadius: UnittedQuantity[Distance, N])(
      implicit angleConverter: AngleConverter[N],
      ctn: ConvertableTo[N],
      angleModule: Module[UnittedQuantity[Angle, N], N],
      distanceModule: Module[UnittedQuantity[Distance, N], N]): UnittedQuantity[Distance, N] =
    sphereRadius :* ((angle in angleConverter.radian).magnitude)

  def sine[N: MultiplicativeMonoid: Eq: Trig](
    a: UnittedQuantity[Angle, N])(
      implicit converter: AngleConverter[N]): N =
    spire.math.sin((a in converter.radian).magnitude)

  def cosine[N: MultiplicativeMonoid: Eq: Trig](
    a: UnittedQuantity[Angle, N])(
      implicit converter: AngleConverter[N]): N =
    spire.math.cos((a in converter.radian).magnitude)

  def tangent[N: MultiplicativeMonoid: Eq: Trig](
    a: UnittedQuantity[Angle, N])(
      implicit converter: AngleConverter[N]): N =
    spire.math.tan((a in converter.radian).magnitude)

  def arcTangent[N: Trig](x: N)(
    implicit converter: AngleConverter[N]): UnittedQuantity[Angle, N] =
    spire.math.atan(x) *: converter.radian

  def arcTangent2[N: Trig](x: N, y: N)(
    implicit converter: AngleConverter[N]): UnittedQuantity[Angle, N] =
    spire.math.atan2(x, y) *: converter.radian

  def arcCosine[N: Trig](x: N)(
    implicit converter: AngleConverter[N]): UnittedQuantity[Angle, N] =
    spire.math.acos(x) *: converter.radian

  def arcSine[N: Trig](x: N)(
    implicit converter: AngleConverter[N]): UnittedQuantity[Angle, N] =
    spire.math.asin(x) *: converter.radian

  implicit def enrichGenSeq[T](genSeq: collection.GenSeq[T]): EnrichedGenSeq[T] = EnrichedGenSeq(genSeq)

  implicit def enrichGenTraversable[T: Manifest](gt: collection.GenTraversable[T]): EnrichedGenTraversable[T] = EnrichedGenTraversable(gt)

  implicit def enrichIndexedSeq[T: Manifest](is: IndexedSeq[T]): EnrichedIndexedSeq[T] = EnrichedIndexedSeq(is)

  implicit def enrichIterator[T](it: Iterator[T]) = new EnrichedIterator(it)

  implicit def enrichByteArray(barr: Array[Byte]): EnrichedByteArray = EnrichedByteArray(barr)

  implicit def enrichMutableBuffer[T](buffer: Buffer[T]): EnrichedMutableBuffer[T] = EnrichedMutableBuffer(buffer)

  implicit def enrichArray[T: Manifest](arr: Array[T]): EnrichedArray[T] = EnrichedArray(arr)

  implicit def enrichInt(n: Int): EnrichedInt = EnrichedInt(n)

  def fib(n: Int): Int = (1 to n).foldLeft((1, 1))((pre, i) => (pre._2, pre._1 + pre._2))._1

  def recfib(n: Int): Int = n match { case 0 | 1 => 1 case _ => recfib(n - 2) + recfib(n - 1) }

  /**
   * http://en.wikipedia.org/wiki/Ackermann_function
   */

  def ackermann(m: Long, n: Long): Long = {

    if (m === 0L) {
      n + 1
    } else if (m > 0 && n === 0L) {
      ackermann(m - 1, 1)
    } else {
      ackermann(m - 1, ackermann(m, n - 1))
    }
  }

  /**
   * https://en.wikipedia.org/wiki/Logistic_map
   */

  def logisticMap[N: Ring](λ: N): N => N = {
    x => λ * x * (Ring[N].one - x)
  }

  /**
   * https://en.wikipedia.org/wiki/Mandelbrot_set
   *
   */

  def mandelbrotNext[N](R: N, I: N)(
    implicit rng: Rng[N]): ((N, N)) => (N, N) = (nn: (N, N)) => {
    import rng.plus
    import rng.minus
    import rng.times
    val c: N = plus(minus(times(nn._1, nn._1), times(nn._2, nn._2)), R)
    val i: N = plus(plus(times(nn._1, nn._2), times(nn._1, nn._2)), I)
    (c, i)
  }

  def mandelbrotContinue[N](radius: N)(
    implicit rng: Rng[N],
    o: Order[N]): ((N, N)) => Boolean = (c: (N, N)) => {
    import rng.times
    import rng.plus
    o.lteqv(plus(times(c._1, c._1), (times(c._2, c._2))), radius)
  }

  def inMandelbrotSet[N](radius: N, R: N, I: N, maxIt: Int)(
    implicit rng: Rng[N],
    o: Order[N]): Boolean =
    applyForever(mandelbrotNext(R, I), (rng.zero, rng.zero))
      .takeWhile(mandelbrotContinue(radius) _)
      .terminatesWithin(maxIt)

  def inMandelbrotSetAt[N](radius: N, R: N, I: N, maxIt: Int)(
    implicit rng: Rng[N],
    o: Order[N]): Option[Int] =
    applyForever(mandelbrotNext(R, I), (rng.zero, rng.zero))
      .takeWhile(mandelbrotContinue(radius) _)
      .take(maxIt)
      .zipWithIndex
      .lastOption
      .flatMap({ l => if (l._2 + 1 < maxIt) Some(l._2) else None })

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

  // more math

  def square[N: Field](x: N): N = x ** 2

  def √[N: NRoot](x: N): N = x.sqrt

  //  implicit def eqSet[S: Eq]: Eq[Set[S]] = new Eq[Set[S]] {
  //    def eqv(x: Set[S], y: Set[S]): Boolean = (x.size === y.size) && x.intersect(y).size === x.size
  //  }
  //
  //  implicit def eqIndexedSeq[T: Eq]: Eq[IndexedSeq[T]] = new Eq[IndexedSeq[T]] {
  //    def eqv(x: IndexedSeq[T], y: IndexedSeq[T]): Boolean = {
  //      val lhs = (x.size == y.size)
  //      val rhs = (x.zip(y).forall({ case (a, b) => a === b }))
  //      lhs && rhs
  //    }
  //  }
  //
  //  implicit def eqSeq[T: Eq]: Eq[Seq[T]] = new Eq[Seq[T]] {
  //    def eqv(x: Seq[T], y: Seq[T]): Boolean = {
  //      val lhs = (x.size == y.size)
  //      val rhs = (x.zip(y).forall({ case (a, b) => a === b }))
  //      lhs && rhs
  //    }
  //  }

  def string[T: Show](t: T): String = Show[T].show(t)

  def show[T: Show](t: T): String = Show[T].show(t)

  def print[T: Show](t: T): Unit = println(string(t))

  def html[T: HtmlFrom](t: T): xml.Node = HtmlFrom[T].toHtml(t)

}
