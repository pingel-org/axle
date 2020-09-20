package axle

import cats.Functor
import cats.kernel.Eq
import cats.kernel.Order
import cats.implicits._

//import spire.implicits.additiveGroupOps
import spire.implicits.multiplicativeGroupOps
//import spire.implicits.multiplicativeSemigroupOps
import spire.math.log
//import spire.math.Rational
//import spire.math.Rational.apply
import spire.math.Real.apply
import spire.algebra._
import spire.implicits.nrootOps
import spire.implicits.semiringOps
import spire.implicits.multiplicativeSemigroupOps
import spire.implicits.additiveGroupOps
import spire.implicits.rightModuleOps
import spire.math.Rational
import spire.math.ConvertableTo
import spire.math.ConvertableFrom

import axle.quanta.Angle
import axle.quanta.UnittedQuantity
import axle.quanta.AngleConverter
import axle.quanta.Distance
import axle.algebra._

import axle.syntax.finite.finiteOps
import axle.syntax.indexed.indexedOps

package object math {

  def showDoubleWithPrecision(p: Int = 6): Show[Double] = d => {
    val fmt = s"""%.${p}f"""
    fmt.format(d)
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
   * Englishman John Wallis (1616 - 1703) approximation of π in 1655
   *
   */
  def wallisΠ(iterations: Int = 10000) =
    2 * Π[Rational, IndexedSeq]((1 to iterations) map { n =>
      val nl = n.toLong
      Rational(
        (2L * nl) * (2L * nl),
        (2L * nl - 1L) * (2L * nl + 1L))
    })

  /**
   * Monte Carlo approximation of pi http://en.wikipedia.org/wiki/Monte_Carlo_method
   *
   * TODO get n2v implicitly?
   *
   */

  def monteCarloPiEstimate[F[_], N, V: ConvertableTo](
    trials: F[N],
    n2v:    N => V)(
    implicit
    finite:  Finite[F, N],
    functor: Functor[F],
    agg:     Aggregatable[F],
    field:   Field[V]): V = {

    import spire.math.random
    //    import spire.implicits.multiplicativeSemigroupOps
    //    import spire.implicits.multiplicativeGroupOps

    val randomPointInCircle: () => V = () => {
      val x = random() * 2 - 1
      val y = random() * 2 - 1
      if (x * x + y * y < 1) field.one else field.zero
    }

    val vFour = ConvertableTo[V].fromDouble(4d)

    val counts: F[V] = trials.map(i => randomPointInCircle())

    val s: V = Σ(counts)

    val numerator: V = vFour * s

    val denominator: V = n2v(finite.size(trials))

    numerator / denominator
  }

  def distanceOnSphere[N: MultiplicativeMonoid](
    angle:        UnittedQuantity[Angle, N],
    sphereRadius: UnittedQuantity[Distance, N])(
    implicit
    angleConverter: AngleConverter[N],
    //ctn: ConvertableTo[N],
    //angleModule: Module[UnittedQuantity[Angle, N], N],
    distanceModule: CModule[UnittedQuantity[Distance, N], N]): UnittedQuantity[Distance, N] =
    sphereRadius :* ((angle in angleConverter.radian).magnitude)

  def sine[N: MultiplicativeMonoid: Eq: Trig](
    a: UnittedQuantity[Angle, N])(
    implicit
    converter: AngleConverter[N]): N =
    spire.math.sin((a in converter.radian).magnitude)

  def cosine[N: MultiplicativeMonoid: Eq: Trig](
    a: UnittedQuantity[Angle, N])(
    implicit
    converter: AngleConverter[N]): N =
    spire.math.cos((a in converter.radian).magnitude)

  def tangent[N: MultiplicativeMonoid: Eq: Trig](
    a: UnittedQuantity[Angle, N])(
    implicit
    converter: AngleConverter[N]): N =
    spire.math.tan((a in converter.radian).magnitude)

  def arcTangent[N: Trig](x: N)(
    implicit
    converter: AngleConverter[N]): UnittedQuantity[Angle, N] =
    spire.math.atan(x) *: converter.radian

  def arcTangent2[N: Trig](x: N, y: N)(
    implicit
    converter: AngleConverter[N]): UnittedQuantity[Angle, N] =
    spire.math.atan2(x, y) *: converter.radian

  def arcCosine[N: Trig](x: N)(
    implicit
    converter: AngleConverter[N]): UnittedQuantity[Angle, N] =
    spire.math.acos(x) *: converter.radian

  def arcSine[N: Trig](x: N)(
    implicit
    converter: AngleConverter[N]): UnittedQuantity[Angle, N] =
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
    implicit
    multB:    MultiplicativeSemigroup[B],
    eucRingN: EuclideanRing[N], eqN: Eq[N]): B = {

    import eucRingN.{ one, zero }

    val two = eucRingN.plus(one, one)

    if (eqN.eqv(pow, one)) {
      base
    } else if (eqN.eqv(eucRingN.emod(pow, two), zero)) {
      val half = exponentiateByRecursiveSquaring(base, eucRingN.equot(pow, two))
      half * half
    } else {
      val half = exponentiateByRecursiveSquaring(base, eucRingN.equot(eucRingN.minus(pow, one), two))
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
    implicit
    rng: Rng[N]): ((N, N)) => (N, N) = (nn: (N, N)) => {
    import rng.plus
    import rng.minus
    import rng.times
    val c: N = plus(minus(times(nn._1, nn._1), times(nn._2, nn._2)), R)
    val i: N = plus(plus(times(nn._1, nn._2), times(nn._1, nn._2)), I)
    (c, i)
  }

  def mandelbrotContinue[N](radius: N)(
    implicit
    rng: Rng[N],
    o:   Order[N]): ((N, N)) => Boolean = (c: (N, N)) => {
    import rng.times
    import rng.plus
    o.lteqv(plus(times(c._1, c._1), (times(c._2, c._2))), radius)
  }

  def inMandelbrotSet[N](radius: N, R: N, I: N, maxIt: Int)(
    implicit
    rng: Rng[N],
    o:   Order[N]): Boolean =
    applyForever(mandelbrotNext(R, I), (rng.zero, rng.zero))
      .takeWhile(mandelbrotContinue(radius) _)
      .terminatesWithin(maxIt)

  def inMandelbrotSetAt[N](radius: N, R: N, I: N, maxIt: Int)(
    implicit
    rng: Rng[N],
    o:   Order[N]): Option[Int] =
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
        LazyList.from(0).map(i * i + i * _).takeWhile(_ < n) foreach { j =>
          A(j) = false
        }
      }
    }

    (2 until n) filter { A }
  }
  def notPrimeUpTo[N](n: N)(implicit orderN: Order[N], ringN: Ring[N]): LazyList[N] = {

    val two = ringN.plus(ringN.one, ringN.one)

    val bases = lazyListsFrom(two).takeWhile(i => ringN.times(i, i) < n)

    val notPrimeStreams =
      filterOut(bases, if (!bases.isEmpty) notPrimeUpTo(bases.last) else LazyList.empty) map { i =>
        lazyListsFrom(ringN.zero).map(j => ringN.plus(i * i, i * j))
      }

    mergeStreams(notPrimeStreams)
  }

  def primeStream[N](n: N)(implicit orderN: Order[N], ringN: Ring[N]): LazyList[N] = {

    require(n > ringN.one)

    val two = ringN.plus(ringN.one, ringN.one)
    filterOut(lazyListsFrom(two).takeWhile(i => i < n), notPrimeUpTo(n))
  }

  def log2[N: Field: ConvertableFrom](x: N): Double = log(ConvertableFrom[N].toDouble(x)) / log(2d)

  def square[N: Ring](x: N): N = x ** 2

  def √[N: NRoot](x: N): N = x.sqrt

  def argmax[R[_], K, N: Order](
    ks: R[K],
    f:  K => N)(
    implicit
    functor:   Functor[R],
    redicible: Reducible[R]): Option[K] = {

    val mapped = functor.map(ks)(k => (k, f(k)))
    // TODO: This could be extracted as Reducible.maxBy

    redicible.reduceOption(mapped)({
      case (kv1, kv2) =>
        if (kv1._2 > kv2._2) kv1 else kv2
    }).map(_._1)
  }

  //.maxBy(_._2)._1

  def Σ[A, F[_]](fa: F[A])(implicit ev: AdditiveMonoid[A], agg: Aggregatable[F]): A =
    agg.aggregate(fa)(ev.zero)(ev.plus, ev.plus)

  def sum[A, F[_]](fa: F[A])(implicit ev: AdditiveMonoid[A], agg: Aggregatable[F]): A =
    agg.aggregate(fa)(ev.zero)(ev.plus, ev.plus)

  val Sigma = Σ _

  def Π[A, F[_]](fa: F[A])(implicit ev: MultiplicativeMonoid[A], agg: Aggregatable[F]): A =
    agg.aggregate(fa)(ev.one)(ev.times, ev.times)

  def product[A, F[_]](fa: F[A])(implicit ev: MultiplicativeMonoid[A], agg: Aggregatable[F]): A =
    agg.aggregate(fa)(ev.one)(ev.times, ev.times)

  val Pi = Π _

  /**
   * arithmetic, geometric, and harmonic means are "Pythagorean"
   *
   * https://en.wikipedia.org/wiki/Pythagorean_means
   *
   */

  def mean[N, F[_]](ns: F[N])(
    implicit
    field:        Field[N],
    aggregatable: Aggregatable[F],
    finite:       Finite[F, N]): N =
    arithmeticMean[N, F](ns)

  def arithmeticMean[N, F[_]](ns: F[N])(
    implicit
    field:        Field[N],
    aggregatable: Aggregatable[F],
    finite:       Finite[F, N]): N =
    Σ(ns) / ns.size

  def geometricMean[N, F[N]](ns: F[N])(
    implicit
    ev:    MultiplicativeMonoid[N],
    agg:   Aggregatable[F],
    fin:   Finite[F, Int],
    nroot: NRoot[N]): N =
    nroot.nroot(Π(ns), ns.size)

  def harmonicMean[N, F[_]](ns: F[N])(
    implicit
    field:       Field[N],
    functorFaaF: Functor[F],
    agg:         Aggregatable[F],
    fin:         Finite[F, N]): N =
    ns.size / Σ(functorFaaF.map(ns)(field.reciprocal))

  /**
   * Generalized mean
   *
   * https://en.wikipedia.org/wiki/Generalized_mean
   *
   * TODO could be special-cased for p = -∞ or ∞
   */

  def generalizedMean[N, F[_]](p: N, ns: F[N])(
    implicit
    field:   Field[N],
    functor: Functor[F],
    agg:     Aggregatable[F],
    fin:     Finite[F, N],
    nroot:   NRoot[N]): N =
    nroot.fpow(
      field.reciprocal(ns.size) * Σ(ns.map(x => nroot.fpow(x, p))),
      field.reciprocal(p))

  /**
   * Generalized f-Mean
   *
   * https://en.wikipedia.org/wiki/Generalized_mean#Generalized_f-mean
   *
   * https://en.wikipedia.org/wiki/Quasi-arithmetic_mean
   *
   * TODO f need only be injective
   */

  def generalizedFMean[N, F[_]](f: Bijection[N, N], ns: F[N])(
    implicit
    field:   Field[N],
    functor: Functor[F],
    agg:     Aggregatable[F],
    fin:     Finite[F, N]): N =
    f.unapply(field.reciprocal(ns.size) * Σ(ns.map(f)))

  def movingArithmeticMean[F[_], I, N](xs: F[N], size: I)(
    implicit
    convert: I => N,
    indexed: Indexed[F, I],
    fin:     Finite[F, N],
    field:   Field[N],
    zipper:  Zipper[F],
    agg:     Aggregatable[F],
    scanner: Scanner[F]): F[N] = {

    val initial: N = arithmeticMean(xs.take(size))

    scanner
      .scanLeft(zipper.zip(xs, xs.drop(size)))(initial)({ (s: N, outIn: (N, N)) =>
        val sumDelta = outIn._2 - outIn._1
        field.plus(s, sumDelta / convert(size))
      })
  }

  def movingGeometricMean[F[_], I, N](xs: F[N], size: I)(
    implicit
    convert: I => Int,
    indexed: Indexed[F, I],
    field:   Field[N],
    zipper:  Zipper[F],
    agg:     Aggregatable[F],
    scanner: Scanner[F],
    fin:     Finite[F, Int],
    nroot:   NRoot[N]): F[N] = {

    val initial: N = geometricMean(xs.take(size))

    scanner
      .scanLeft(zipper.zip(xs, xs.drop(size)))(initial)({ (s: N, outIn: (N, N)) =>
        s * nroot.nroot((outIn._2 / outIn._1), convert(size))
      })
  }

  def movingHarmonicMean[F[_], I, N](xs: F[N], size: I)(
    implicit
    convert: I => N,
    indexed: Indexed[F, I],
    field:   Field[N],
    zipper:  Zipper[F],
    agg:     Aggregatable[F],
    scanner: Scanner[F],
    functor: Functor[F],
    fin:     Finite[F, N]): F[N] = {

    val initial: N = harmonicMean(xs.take(size))

    scanner
      .scanLeft(zipper.zip(xs, xs.drop(size)))(initial)({ (p: N, outIn: (N, N)) =>
        val oldSum = field.reciprocal(p / convert(size))
        val sumDelta = field.reciprocal(outIn._2) - field.reciprocal(outIn._1)
        field.reciprocal(field.plus(oldSum, sumDelta)) * size
      })
  }

  def movingGeneralizedMean[F[_], I, N](p: N, xs: F[N], size: I)(
    implicit
    convert: I => N,
    indexed: Indexed[F, I],
    field:   Field[N],
    zipper:  Zipper[F],
    agg:     Aggregatable[F],
    scanner: Scanner[F],
    functor: Functor[F],
    fin:     Finite[F, N],
    nroot:   NRoot[N]): F[N] = {

    val initial: N = generalizedMean(p, xs.take(size))

    scanner
      .scanLeft(zipper.zip(xs, xs.drop(size)))(initial)({ (s: N, outIn: (N, N)) =>
        val oldSum = nroot.fpow(s, p) * size
        val sumDelta = nroot.fpow(outIn._2, p) - nroot.fpow(outIn._1, p)
        nroot.fpow((field.plus(oldSum, sumDelta) / size), field.reciprocal(p))
      })
  }

  def movingGeneralizedFMean[F[_], I, N](f: Bijection[N, N], xs: F[N], size: I)(
    implicit
    convert: I => N,
    indexed: Indexed[F, I],
    field:   Field[N],
    zipper:  Zipper[F],
    agg:     Aggregatable[F],
    scanner: Scanner[F],
    functor: Functor[F],
    fin:     Finite[F, N]): F[N] = {

    val initial: N = generalizedFMean(f, xs.take(size))

    scanner
      .scanLeft(zipper.zip(xs, xs.drop(size)))(initial)({ (s: N, outIn: (N, N)) =>
        val oldSum = f(s) * size
        val sumDelta = f(outIn._2) - f(outIn._1)
        f.unapply(field.plus(oldSum, sumDelta) / size)
      })
  }
}