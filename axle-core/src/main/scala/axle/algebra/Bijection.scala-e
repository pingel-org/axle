package axle.algebra

import scala.annotation.implicitNotFound

@implicitNotFound("Witness not found for Bijection[${A}, ${B}]")
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

object Bijection {

  final def apply[A, B](implicit ev: Bijection[A, B]): Bijection[A, B] = ev

}
