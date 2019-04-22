package axle.algebra

import scala.language.implicitConversions

import cats.kernel.Eq

import spire.algebra.MultiplicativeSemigroup

object Binary {

  implicit def fromInt(i: Int): Binary = i match {
      case 0 => B0
      case _ => B1
  }
  
  implicit val eqBinary: Eq[Binary] =
    (a: Binary, b: Binary) => (a, b) match {
      case (B0, B0) => true
      case (B1, B1) => true
      case _        => false
    }

  implicit val multiplicativeSemigroupBinary: MultiplicativeSemigroup[Binary] =
    new MultiplicativeSemigroup[Binary] {
      def times(x: Binary, y: Binary): Binary = (x, y) match {
        case (B1, B1) => B1
        case _        => B0
      }
    }

}

sealed trait Binary {
  def negate: Binary
}

object B0 extends Binary {
  def negate = B1
}

object B1 extends Binary {
  def negate = B0
}
