package axle

import cats.kernel.Eq
import cats.kernel.Order
import cats.implicits._
import spire.algebra._
import spire.implicits.moduleOps
import spire.implicits.nrootOps
import spire.implicits.semiringOps
import spire.implicits.multiplicativeSemigroupOps
import spire.implicits.additiveGroupOps
import spire.math.Rational
import spire.math.ConvertableTo
import axle.quanta.Angle
import axle.quanta.UnittedQuantity
import axle.quanta.AngleConverter
import axle.quanta.Distance
import axle.algebra.Aggregatable
import axle.algebra.Finite
import axle.algebra.Functor
import axle.algebra.Π

package object math {

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

  def fibonacciByFold(n: Int): Int =
    (1 to n).foldLeft((1, 1))((pre, i) => (pre._2, pre._1 + pre._2))._1

  def fibonacciRecursively(n: Int): Int =
    n match {
      case 0 | 1 => 1
      case _     => fibonacciRecursively(n - 2) + fibonacciRecursively(n - 1)
    }

  import spire.implicits.multiplicativeSemigroupOps

  def exponentiateByRecursiveSquaring[B, N](base: B, pow: N)(
    implicit multB: MultiplicativeSemigroup[B],
    eucRingN: EuclideanRing[N], eqN: Eq[N]): B = {

    import eucRingN.{ one, zero }

    val two = eucRingN.plus(one, one)

    if (eqN.eqv(pow, one)) {
      base
    } else if (eqN.eqv(eucRingN.mod(pow, two), zero)) {
      val half = exponentiateByRecursiveSquaring(base, eucRingN.quot(pow, two))
      half * half
    } else {
      val half = exponentiateByRecursiveSquaring(base, eucRingN.quot(eucRingN.minus(pow, one), two))
      half * half * base
    }
  }

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

  /**
   * Sieve of Eratosthenes
   *
   * The old-fashioned mutable way, described in pseudocode here:
   *
   *   https://en.wikipedia.org/wiki/Sieve_of_Eratosthenes
   *
   * input must be greater than one
   */

  def sieveOfEratosthenes(n: Int): Seq[Int] = {

    require(n > 1)

    import spire.math.{ sqrt, floor }

    val A = new Array[Boolean](n)
    (2 until n) foreach { i => A(i) = true }

    (2 to floor(sqrt(n.toDouble)).toInt) foreach { i =>
      if (A(i)) {
        Stream.from(0).map(i * i + i * _).takeWhile(_ < n) foreach { j =>
          A(j) = false
        }
      }
    }

    (2 until n) filter { A }
  }
  def notPrimeUpTo[N](n: N)(implicit orderN: Order[N], ringN: Ring[N]): Stream[N] = {

    val two = ringN.plus(ringN.one, ringN.one)

    val bases = streamFrom(two).takeWhile(i => ringN.times(i, i) < n)

    val notPrimeStreams =
      filterOut(bases, if (!bases.isEmpty) notPrimeUpTo(bases.last) else Stream.empty) map { i =>
        streamFrom(ringN.zero).map(j => ringN.plus(i * i, i * j))
      }

    mergeStreams(notPrimeStreams)
  }

  def primeStream[N](n: N)(implicit orderN: Order[N], ringN: Ring[N]): Stream[N] = {

    require(n > ringN.one)

    val two = ringN.plus(ringN.one, ringN.one)
    filterOut(streamFrom(two).takeWhile(i => i < n), notPrimeUpTo(n))
  }

  def square[N: Field](x: N): N = x ** 2

  def √[N: NRoot](x: N): N = x.sqrt

}