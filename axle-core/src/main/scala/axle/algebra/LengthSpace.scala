package axle.algebra

import scala.annotation.implicitNotFound
import spire.algebra.MetricSpace
import spire.math.Rational
import spire.std.DoubleAlgebra
import spire.std.IntAlgebra
import spire.std.LongAlgebra

/**
 *
 * http://en.wikipedia.org/wiki/Intrinsic_metric
 *
 */

@implicitNotFound("Witness not found for LengthSpace[${V}, ${R}]")
trait LengthSpace[V, R, P] extends MetricSpace[V, R] {

  // TODO: possibly make Double (below) a type-parameter

  /**
   *
   * also known as γ
   *
   * p is in [0, 1]
   *
   */

  def onPath(left: V, right: V, p: P): V

  /**
   *
   * the inverse of onPath
   *
   * returns a number between 0 and 1 of type P
   */

  def portion(left: V, v: V, right: V): P

}

object LengthSpace {

  final def apply[V, R, P](implicit ev: LengthSpace[V, R, P]): LengthSpace[V, R, P] = ev

  import axle.quanta.UnittedQuantity
  import axle.quanta.UnitConverter
  import spire.algebra.MultiplicativeMonoid
  import spire.algebra.Eq

  // TODO move uqDoubleLengthSpace to UnittedQuantity.scala
  // it also seems like this wrapped lengthspace could be generalized
  implicit def uqDoubleLengthSpace[Q, V: MultiplicativeMonoid: Eq, R, P](
    implicit vrpls: LengthSpace[V, R, P], uc: UnitConverter[Q, V]): LengthSpace[UnittedQuantity[Q, V], R, P] =
    new DoubleAlgebra with LengthSpace[UnittedQuantity[Q, V], R, P] {

      def onPath(left: UnittedQuantity[Q, V], right: UnittedQuantity[Q, V], p: P): UnittedQuantity[Q, V] =
        UnittedQuantity(vrpls.onPath(left.magnitude, (right in left.unit).magnitude, p), left.unit)

      def portion(left: UnittedQuantity[Q, V], v: UnittedQuantity[Q, V], right: UnittedQuantity[Q, V]): P =
        vrpls.portion(left.magnitude, (v in left.unit).magnitude, (right in right.unit).magnitude)

      def distance(v: UnittedQuantity[Q, V], w: UnittedQuantity[Q, V]): R =
        vrpls.distance(v.magnitude, (w in v.unit).magnitude)
    }

  implicit val doubleDoubleLengthSpace: LengthSpace[Double, Double, Double] =
    new DoubleAlgebra with LengthSpace[Double, Double, Double] {

      def distance(v: Double, w: Double): Double = math.abs(v - w)

      def onPath(left: Double, right: Double, p: Double): Double = (right - left) * p + left

      def portion(left: Double, v: Double, right: Double): Double = (v - left) / (right - left)
    }

  implicit val longLongLengthSpace: LengthSpace[Long, Long, Double] =
    new LongAlgebra with LengthSpace[Long, Long, Double] {

      def distance(v: Long, w: Long): Long = math.abs(v - w)

      def onPath(left: Long, right: Long, p: Double): Long = ((right - left) * p + left).toLong

      def portion(left: Long, v: Long, right: Long): Double = (v - left).toDouble / (right - left)
    }

  implicit val intIntLengthSpace: LengthSpace[Int, Int, Double] =
    new IntAlgebra with LengthSpace[Int, Int, Double] {

      def distance(v: Int, w: Int): Int =
        spire.math.abs(v - w)

      def onPath(left: Int, right: Int, p: Double): Int =
        ((right - left) * p + left).toInt

      def portion(left: Int, v: Int, right: Int): Double =
        (v - left).toDouble / (right - left)
    }

  implicit val rationalRationalLengthSpace: LengthSpace[Rational, Rational, Double] =
    new LengthSpace[Rational, Rational, Double] {

      def distance(v: Rational, w: Rational): Rational =
        (v - w).abs

      def onPath(left: Rational, right: Rational, p: Double): Rational =
        (right - left) * p + left

      def portion(left: Rational, v: Rational, right: Rational): Double =
        ((v - left) / (right - left)).toDouble
    }

}