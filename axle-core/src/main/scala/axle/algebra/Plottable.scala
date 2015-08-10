package axle.algebra

import java.lang.Double.isInfinite
import java.lang.Double.isNaN
import scala.annotation.implicitNotFound

import spire.algebra.Order
import spire.algebra.Field
import spire.algebra.Eq
import spire.math.Rational

@implicitNotFound("Witness not found for Plottable[${T}]")
trait Plottable[T] {

  def isPlottable(t: T): Boolean = true
}

object Plottable {

  final def apply[T: Plottable]: Plottable[T] = implicitly[Plottable[T]]

  implicit val doublePlottable: Plottable[Double] =
    new Plottable[Double] {
      override def isPlottable(t: Double): Boolean = (!t.isInfinite) && (!t.isNaN)
    }

  implicit val longPlottable: Plottable[Long] = new Plottable[Long] {}

  implicit val intPlottable: Plottable[Int] = new Plottable[Int] {}

  implicit val rationalPlottable: Plottable[Rational] =
    new Plottable[Rational] {
      override def isPlottable(t: Rational): Boolean = {
        val d = t.toDouble
        !isInfinite(d) && !isNaN(d)
      }
    }

}
