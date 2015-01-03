package axle

import scala.reflect.ClassTag

import axle.algebra.Aggregatable

import spire.algebra.AdditiveMonoid
import spire.algebra.Field
import spire.algebra.MetricSpace
import spire.algebra.MultiplicativeMonoid
import spire.algebra.Order
import spire.compat.ordering
import spire.math.Rational
import spire.math.Real

/**
 *
 * http://en.wikipedia.org/wiki/Algebraic_structure
 *
 */

import spire.implicits._

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

  def mean[A: ClassTag, F[_]](fa: F[A])(implicit ev: Field[A], agg: Aggregatable[F], fin: Finite[F]): A =
    sum(fa) / fin.size(fa)

  def Π[A: ClassTag, F[_]](fa: F[A])(implicit ev: MultiplicativeMonoid[A], agg: Aggregatable[F]): A =
    agg.aggregate(fa)(ev.one)(ev.times, ev.times)

  def product[A: ClassTag, F[_]](fa: F[A])(implicit ev: MultiplicativeMonoid[A], agg: Aggregatable[F]): A =
    agg.aggregate(fa)(ev.one)(ev.times, ev.times)

  implicit val rationalDoubleMetricSpace: MetricSpace[Rational, Double] = new MetricSpace[Rational, Double] {

    def distance(v: Rational, w: Rational): Double = (v.toDouble - w.toDouble).abs
  }

  implicit val realDoubleMetricSpace: MetricSpace[Real, Double] = new MetricSpace[Real, Double] {

    def distance(v: Real, w: Real): Double = (v.toDouble - w.toDouble).abs
  }

  implicit val doubleDoubleMetricSpace: MetricSpace[Double, Double] = new MetricSpace[Double, Double] {

    def distance(v: Double, w: Double): Double = (v - w).abs
  }

}
