package axle.algebra

import scala.annotation.implicitNotFound

import spire.algebra.AdditiveMonoid
import spire.algebra.Ring

/**
 * Zero
 *
 * ∅
 *
 */

@implicitNotFound("Witness not found for Zero[${T}]")
trait Zero[T] {

  def zero: T

}

object Zero {

  final def apply[T: Zero]: Zero[T] = implicitly[Zero[T]]

  implicit def ringZero[T: Ring]: Zero[T] =
    new Zero[T] {
      val z: T = Ring[T].additive.empty
      def zero: T = z
    }

  /**
   * Note: temporarily making additiveMonoidZero not implicit to avoid ambiguity
   * with ringZeor (above).
   *
   * spire.algebra.Ring.additive now returns an algebra.CommutativeGroup,
   * which is seemingly the easiest way to get, so the proper technique for
   * finding and AdditiveMomoid now now unclear.
   *
   * Until that is resolved, ringZero will be the preferred (and admittedly over-constrained)
   * method.
   */
  def additiveMonoidZero[T: AdditiveMonoid]: Zero[T] =
    new Zero[T] {
      val z = implicitly[AdditiveMonoid[T]].zero
      def zero: T = z
    }

}
