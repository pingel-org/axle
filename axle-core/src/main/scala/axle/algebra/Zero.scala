package axle.algebra

import scala.annotation.implicitNotFound

import spire.algebra.AdditiveMonoid

/**
 * Zero
 * 
 * ∅
 * 
 */

@implicitNotFound("No member of typeclass Zero found for type ${T}")
trait Zero[T] {

  def zero: T

}

object Zero {

  def apply[T: Zero]: Zero[T] = Zero[T]

  implicit def addemZero[T: AdditiveMonoid]: Zero[T] =
    new Zero[T] {
      def zero: T = implicitly[AdditiveMonoid[T]].zero
    }

}