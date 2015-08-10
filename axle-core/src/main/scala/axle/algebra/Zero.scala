package axle.algebra

import scala.annotation.implicitNotFound

import spire.algebra.AdditiveMonoid

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

  implicit def addemZero[T: AdditiveMonoid]: Zero[T] =
    new Zero[T] {
      def zero: T = implicitly[AdditiveMonoid[T]].zero
    }

}