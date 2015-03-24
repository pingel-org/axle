package axle

import scala.reflect.ClassTag

import axle.algebra.Aggregatable
import axle.algebra.Finite
import axle.algebra.Functor
import axle.algebra.Reducible
import spire.algebra.AdditiveGroup
import spire.algebra.AdditiveMonoid
import spire.algebra.Field
import spire.algebra.MetricSpace
import spire.algebra.Module
import spire.algebra.MultiplicativeMonoid
import spire.algebra.MultiplicativeSemigroup
import spire.algebra.Order
import spire.algebra.Rng
import spire.implicits.DoubleAlgebra
import spire.implicits.multiplicativeGroupOps
import spire.implicits.partialOrderOps
import spire.math.ConvertableTo
import spire.math.Rational
import spire.math.Real

package object algebra {

  def argmax[R[_]: Functor: Reducible, K, N: Order](ks: R[K], f: K => N): Option[K] = {

    val functor = implicitly[Functor[R]]
    val reducer = implicitly[Reducible[R]]
    val mapped = functor.map(ks)(k => (k, f(k)))
    // TODO: This could be extracted as Reducible.maxBy

    reducer.reduceOption(mapped)({
      case (kv1, kv2) =>
        if (kv1._2 > kv2._2) kv1 else kv2
    }).map(_._1)
  }

  //.maxBy(_._2)._1

  def Σ[A: ClassTag, F[_]](fa: F[A])(implicit ev: AdditiveMonoid[A], agg: Aggregatable[F]): A =
    agg.aggregate(fa)(ev.zero)(ev.plus, ev.plus)

  def sum[A: ClassTag, F[_]](fa: F[A])(implicit ev: AdditiveMonoid[A], agg: Aggregatable[F]): A =
    agg.aggregate(fa)(ev.zero)(ev.plus, ev.plus)

  def Π[A: ClassTag, F[_]](fa: F[A])(implicit ev: MultiplicativeMonoid[A], agg: Aggregatable[F]): A =
    agg.aggregate(fa)(ev.one)(ev.times, ev.times)

  def product[A: ClassTag, F[_]](fa: F[A])(implicit ev: MultiplicativeMonoid[A], agg: Aggregatable[F]): A =
    agg.aggregate(fa)(ev.one)(ev.times, ev.times)

  def mean[A: ClassTag, F[_]](fa: F[A])(implicit ev: Field[A], agg: Aggregatable[F], fin: Finite[F]): A =
    sum(fa) / fin.size(fa)

  def harmonicMean[A: ClassTag, F[_]](xs: F[A])(
    implicit ct: ConvertableTo[A],
    field: Field[A],
    fun: Functor[F],
    agg: Aggregatable[F],
    fin: Finite[F]): A =
    field.div(ct.fromLong(fin.size(xs)), sum(fun.map(xs)(field.reciprocal)))

  implicit val rationalDoubleMetricSpace: MetricSpace[Rational, Double] = new MetricSpace[Rational, Double] {

    def distance(v: Rational, w: Rational): Double = (v.toDouble - w.toDouble).abs
  }

  implicit val realDoubleMetricSpace: MetricSpace[Real, Double] = new MetricSpace[Real, Double] {

    def distance(v: Real, w: Real): Double = (v.toDouble - w.toDouble).abs
  }

  implicit val doubleDoubleMetricSpace: MetricSpace[Double, Double] = new MetricSpace[Double, Double] {

    def distance(v: Double, w: Double): Double = (v - w).abs
  }

  import spire.implicits._
  import spire.math._

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

    implicit val doubleDoubleModule: Module[Double, Double] = new Module[Double, Double] {

      def negate(x: Double): Double = DoubleAlgebra.negate(x)

      def zero: Double = DoubleAlgebra.zero

      def plus(x: Double, y: Double): Double = DoubleAlgebra.plus(x, y)

      implicit def scalar: Rng[Double] = DoubleAlgebra

      def timesl(r: Double, v: Double): Double = r * v

    }

    implicit val doubleRationalModule: Module[Double, Rational] = new Module[Double, Rational] {

      def negate(x: Double): Double = DoubleAlgebra.negate(x)

      def zero: Double = DoubleAlgebra.zero

      def plus(x: Double, y: Double): Double = DoubleAlgebra.plus(x, y)

      implicit def scalar: Rng[Rational] = rat

      def timesl(r: Rational, v: Double): Double = r.toDouble * v

    }

    implicit val rationalDoubleModule: Module[Rational, Double] = new Module[Rational, Double] {

      def negate(x: Rational): Rational = rat.negate(x)

      def zero: Rational = rat.zero

      def plus(x: Rational, y: Rational): Rational = rat.plus(x, y)

      implicit def scalar: Rng[Double] = DoubleAlgebra

      def timesl(r: Double, v: Rational): Rational = r * v

    }

    implicit val rationalRationalModule: Module[Rational, Rational] = new Module[Rational, Rational] {

      def negate(x: Rational): Rational = rat.negate(x)

      def zero: Rational = rat.zero

      def plus(x: Rational, y: Rational): Rational = rat.plus(x, y)

      implicit def scalar: Rng[Rational] = rat

      def timesl(r: Rational, v: Rational): Rational = r * v

    }

  }
}
