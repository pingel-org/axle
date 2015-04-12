package axle.algebra

import scala.annotation.implicitNotFound

@implicitNotFound("No member of typeclass Bijection found for types ${A}, ${B}")
trait Bijection[A, B] extends Function1[A, B] {

  self =>

  def apply(a: A): B

  def unapply(b: B): A

  def bidirectionallyAndThen[C](other: Bijection[B, C]): Bijection[A, C] =
    new Bijection[A, C] {

      val forward: A => C = (self.apply _).andThen(other.apply _)

      val backward: C => A = (other.unapply _).andThen(self.unapply _)

      def apply(a: A): C = forward(a)

      def unapply(c: C): A = backward(c)

    }

}
