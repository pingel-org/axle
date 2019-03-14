package axle

import scala.language.implicitConversions

import cats.Functor

import spire.algebra._
import spire.implicits.additiveGroupOps

import spire.math.Rational
import spire.math.Rational.apply
import spire.math.Real
import spire.math.Real.apply

package object algebra {

  implicit def catsifyAdditiveGroup[T](ag: _root_.algebra.ring.AdditiveGroup[T]): cats.kernel.Group[T] =
    new cats.kernel.Group[T] {
      def inverse(a: T): T = ag.negate(a)
      def empty: T = ag.zero
      def combine(x: T, y: T): T = ag.plus(x, y)
    }

  implicit def eqIndexedSeq[T](implicit eqT: Eq[T]): Eq[IndexedSeq[T]] =
    (l: IndexedSeq[T], r: IndexedSeq[T]) =>
      l.size == r.size && (0 until l.size).forall( i => eqT.eqv(l(i), r(i)))

  implicit val functorIndexedSeq: Functor[IndexedSeq] =
    new Functor[IndexedSeq] {
      def map[A, B](as: IndexedSeq[A])(f: A => B): IndexedSeq[B] =
        as.map(f)
    }

  implicit val functorSeq: Functor[Seq] =
    new Functor[Seq] {
      def map[A, B](as: Seq[A])(f: A => B): Seq[B] =
        as.map(f)
    }

  implicit def wrappedStringSpace[N](
    implicit
    iscSpace: MetricSpace[IndexedSeq[Char], N]): MetricSpace[String, N] =
      (s1: String, s2: String) => iscSpace.distance(s1, s2)

  implicit val rationalDoubleMetricSpace: MetricSpace[Rational, Double] =
    (v: Rational, w: Rational) =>
      (v.toDouble - w.toDouble).abs

  implicit val realDoubleMetricSpace: MetricSpace[Real, Double] =
    (v: Real, w: Real) =>
      (v.toDouble - w.toDouble).abs

  // implicit val doubleDoubleMetricSpace: MetricSpace[Double, Double] =
  //   (v: Double, w: Double) =>
  //     (v - w).abs

  implicit def metricSpaceFromAdditiveGroupSigned[N: AdditiveGroup: Signed]: MetricSpace[N, N] =
    (v: N, w: N) =>
      spire.math.abs(v - w)

  //import spire.math._

  //  implicit val rationalRng: Rng[Rational] = new Rng[Rational] {
  //
  //    val rat = new spire.math.RationalAlgebra()
  //
  //    def negate(x: Rational): Rational = rat.negate(x)
  //
  //    def zero: Rational = rat.zero
  //
  //    def plus(x: Rational, y: Rational): Rational = rat.plus(x, y)
  //
  //    def times(x: Rational, y: Rational): Rational = rat.times(x, y)
  //  }

  object modules {

    val rat = new spire.math.RationalAlgebra()

    implicit val fieldDouble: Field[Double] = spire.implicits.DoubleAlgebra
    implicit val ringInt: Ring[Int] = spire.implicits.IntAlgebra
    implicit val fieldFloat: Field[Float] = spire.implicits.FloatAlgebra

    implicit val doubleIntModule: Module[Double, Int] =
      new Module[Double, Int] {

        def negate(x: Double): Double = fieldDouble.negate(x)

        def zero: Double = fieldDouble.zero

        def plus(x: Double, y: Double): Double = fieldDouble.plus(x, y)

        implicit def scalar: Rng[Int] = ringInt

        def timesl(r: Int, v: Double): Double = r * v

      }

    implicit val doubleDoubleModule: Module[Double, Double] =
      new Module[Double, Double] {

        def negate(x: Double): Double = fieldDouble.negate(x)

        def zero: Double = fieldDouble.zero

        def plus(x: Double, y: Double): Double = fieldDouble.plus(x, y)

        implicit def scalar: Rng[Double] = fieldDouble

        def timesl(r: Double, v: Double): Double = r * v

      }

    implicit val realDoubleModule: Module[Real, Double] =
      new Module[Real, Double] {

        def negate(x: Real): Real = -x

        def zero: Real = Real(0)

        def plus(x: Real, y: Real): Real = x + y

        implicit def scalar: Rng[Double] = fieldDouble

        def timesl(r: Double, v: Real): Real = r * v
      }

    implicit val realRationalModule: Module[Real, Rational] =
      new Module[Real, Rational] {

        def negate(x: Real): Real = -x

        def zero: Real = Real(0)

        def plus(x: Real, y: Real): Real = x + y

        implicit def scalar: Rng[Rational] = rat

        def timesl(r: Rational, v: Real): Real = r * v
      }

    implicit val doubleRationalModule: Module[Double, Rational] = new Module[Double, Rational] {

      def negate(x: Double): Double = fieldDouble.negate(x)

      def zero: Double = fieldDouble.zero

      def plus(x: Double, y: Double): Double = fieldDouble.plus(x, y)

      implicit def scalar: Rng[Rational] = rat

      def timesl(r: Rational, v: Double): Double = r.toDouble * v

    }

    implicit val floatRationalModule: Module[Float, Rational] = new Module[Float, Rational] {

      def negate(x: Float): Float = fieldFloat.negate(x)

      def zero: Float = fieldFloat.zero

      def plus(x: Float, y: Float): Float = fieldFloat.plus(x, y)

      implicit def scalar: Rng[Rational] = rat

      def timesl(r: Rational, v: Float): Float = r.toDouble.toFloat * v

    }

    implicit val floatDoubleModule: Module[Float, Double] =
      new Module[Float, Double] {

        def negate(x: Float): Float = fieldFloat.negate(x)

        def zero: Float = fieldFloat.zero

        def plus(x: Float, y: Float): Float = fieldFloat.plus(x, y)

        implicit def scalar: Rng[Double] = fieldDouble

        def timesl(r: Double, v: Float): Float = (r * v).toFloat

      }

    implicit val rationalDoubleModule: Module[Rational, Double] = new Module[Rational, Double] {

      def negate(x: Rational): Rational = rat.negate(x)

      def zero: Rational = rat.zero

      def plus(x: Rational, y: Rational): Rational = rat.plus(x, y)

      implicit def scalar: Rng[Double] = fieldDouble

      def timesl(r: Double, v: Rational): Rational = r * v

    }

  }
}
