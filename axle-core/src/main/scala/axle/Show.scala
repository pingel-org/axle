package axle

import spire.math.Rational

trait Show[T] {

  def text(t: T): String
}

object Show {
  
  implicit val showDouble: Show[Double] = new Show[Double] {
    def text(d: Double): String = d.toString
  }
  
  implicit val showBD: Show[BigDecimal] = new Show[BigDecimal] {
    def text(bd: BigDecimal): String = bd.toString
  }

  implicit val showLong: Show[Long] = new Show[Long] {
    def text(l: Long): String = l.toString
  }

  implicit val showInt: Show[Int] = new Show[Int] {
    def text(i: Int): String = i.toString
  }

  implicit val showRational: Show[Rational] = new Show[Rational] {
    def text(r: Rational): String = r.toString
  }
  
}