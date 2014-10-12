package axle.algebra

import spire.implicits._
import spire.algebra._
import org.joda.time.DateTime

trait Zero[T] {

  def zero: T

}

object Zero {

  implicit def addemZero[T: AdditiveMonoid]: Zero[T] =
    new Zero[T] {
      def zero: T = implicitly[AdditiveMonoid[T]].zero
    }

  implicit def dateTimeZero: Zero[DateTime] = new Zero[DateTime] {
    def zero: DateTime = new DateTime()
  }

}