package axle

import spire.math.Rational
import scala.annotation.implicitNotFound

@implicitNotFound("Witness not found for Show[${T}]")
trait Show[T] {

  def text(t: T): String
}

object Show {

  @inline final def apply[T: Show]: Show[T] = implicitly[Show[T]]

  implicit val showDouble: Show[Double] = new Show[Double] {
    // TODO: configurable precision
    def text(d: Double): String = """%.6f""".format(d)
  }

  implicit val showSymbol: Show[Symbol] = new Show[Symbol] {
    def text(s: Symbol): String = s.toString
  }

  implicit val showBoolean: Show[Boolean] = new Show[Boolean] {
    def text(b: Boolean): String = b.toString
  }

  implicit val showBD: Show[BigDecimal] = new Show[BigDecimal] {
    def text(bd: BigDecimal): String = bd.toString
  }

  implicit val showLong: Show[Long] = new Show[Long] {
    def text(l: Long): String = l.toString
  }

  implicit val showChar: Show[Char] = new Show[Char] {
    def text(c: Char): String = c.toString
  }

  implicit val showInt: Show[Int] = new Show[Int] {
    def text(i: Int): String = i.toString
  }

  implicit val showString: Show[String] = new Show[String] {
    def text(s: String): String = s
  }

  implicit val showRational: Show[Rational] = new Show[Rational] {
    def text(r: Rational): String = r.toString
  }

  implicit val showNode: Show[xml.Node] = new Show[xml.Node] {
    def text(n: xml.Node): String = n.toString
  }

}