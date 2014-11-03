package axle

import scala.reflect.ClassTag

import axle.algebra.EnrichedMetricSpace
import axle.algebra.Aggregatable
import axle.matrix.JblasMatrixModule

import spire.algebra.AdditiveMonoid
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

package object algebra {

  def argmaxx[K, N: Order](ks: Iterable[K], f: K => N): K =
    ks.map(k => (k, f(k))).maxBy(_._2)._1

  def Σ[A: ClassTag, F[_]](fa: F[A])(implicit ev: AdditiveMonoid[A], agg: Aggregatable[F]): A =
    agg.aggregate(fa)(ev.zero)(ev.plus, ev.plus)

  def sum[A: ClassTag, F[_]](fa: F[A])(implicit ev: AdditiveMonoid[A], agg: Aggregatable[F]): A =
    agg.aggregate(fa)(ev.zero)(ev.plus, ev.plus)
    
  def Π[A: ClassTag, F[_]](fa: F[A])(implicit ev: MultiplicativeMonoid[A], agg: Aggregatable[F]): A =
    agg.aggregate(fa)(ev.one)(ev.times, ev.times)

  def product[A: ClassTag, F[_]](fa: F[A])(implicit ev: MultiplicativeMonoid[A], agg: Aggregatable[F]): A =
    agg.aggregate(fa)(ev.one)(ev.times, ev.times)

  implicit def enrichMetricSpace[T: Manifest](space: MetricSpace[T, Double]): EnrichedMetricSpace[T] =
    new EnrichedMetricSpace(space) with JblasMatrixModule

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
