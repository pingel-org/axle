package axle.algebra

import spire.implicits._
import spire.algebra._

trait Zero[T] {

  def zero: T

}

object Zero {

  //  def ∅[T](implicit m: Monoid[T]): T = m.id

  implicit def addemZero[T: AdditiveMonoid]: Zero[T] =
    new Zero[T] {
      def zero: T = implicitly[AdditiveMonoid[T]].zero
    }

}