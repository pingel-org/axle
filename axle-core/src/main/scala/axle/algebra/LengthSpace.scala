package axle.algebra

import scala.annotation.implicitNotFound
import spire.algebra.MetricSpace
import spire.algebra.Order
import spire.implicits.LongAlgebra
import spire.math.Rational
import spire.math.Rational.apply
import spire.std.DoubleAlgebra
import spire.std.IntAlgebra
import spire.std.LongAlgebra

/**
 *
 * http://en.wikipedia.org/wiki/Intrinsic_metric
 *
 */

@implicitNotFound("Witness not found for LengthSpace[${V}, ${R}]")
trait LengthSpace[V, R] extends MetricSpace[V, R] {

  // TODO: possibly make Double (below) a type-parameter

  /**
   *
   * also known as γ
   *
   * p is in [0, 1]
   *
   */

  def onPath(left: V, right: V, p: Double): V

  /**
   *
   * the inverse of onPath
   *
   * returns a Real in [0, 1]
   */

  def portion(left: V, v: V, right: V): Double

}

object LengthSpace {

  def apply[V, R](implicit ev: LengthSpace[V, R]): LengthSpace[V, R] = ev

  implicit val doubleDoubleLengthSpace: LengthSpace[Double, Double] =
    new DoubleAlgebra with LengthSpace[Double, Double] {

      def distance(v: Double, w: Double): Double = math.abs(v - w)

      def onPath(left: Double, right: Double, p: Double): Double = (right - left) * p + left

      def portion(left: Double, v: Double, right: Double): Double = (v - left) / (right - left)
    }

  implicit val longLongLengthSpace: LengthSpace[Long, Long] =
    new LongAlgebra with LengthSpace[Long, Long] {

      def distance(v: Long, w: Long): Long = math.abs(v - w)

      def onPath(left: Long, right: Long, p: Double): Long = ((right - left) * p + left).toLong

      def portion(left: Long, v: Long, right: Long): Double = (v - left).toDouble / (right - left)
    }

  implicit val intIntLengthSpace: LengthSpace[Int, Int] =
    new IntAlgebra with LengthSpace[Int, Int] {

      def distance(v: Int, w: Int): Int = math.abs(v - w)

      def onPath(left: Int, right: Int, p: Double): Int = ((right - left) * p + left).toInt

      def portion(left: Int, v: Int, right: Int): Double = (v - left).toDouble / (right - left)
    }

  implicit val rationalRationalLengthSpace: LengthSpace[Rational, Rational] =
    new LengthSpace[Rational, Rational] {

      def distance(v: Rational, w: Rational): Rational = (v - w).abs

      def onPath(left: Rational, right: Rational, p: Double): Rational = (right - left) * p + left

      def portion(left: Rational, v: Rational, right: Rational): Double =
        ((v - left) / (right - left)).toDouble
    }

}