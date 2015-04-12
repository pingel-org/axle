package axle.algebra

import scala.annotation.implicitNotFound

import spire.algebra.AdditiveMonoid

@implicitNotFound("No member of typeclass Zero found for type ${T}")
trait Zero[T] {

  def zero: T

}

object Zero {

  def apply[T: Zero]: Zero[T] = implicitly[Zero[T]]

  //  def ∅[T](implicit m: Monoid[T]): T = m.id

  implicit def addemZero[T: AdditiveMonoid]: Zero[T] =
    new Zero[T] {
      def zero: T = implicitly[AdditiveMonoid[T]].zero
    }

}