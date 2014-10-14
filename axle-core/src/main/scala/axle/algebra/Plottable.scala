package axle.algebra

import java.lang.Double.isInfinite
import java.lang.Double.isNaN

import org.joda.time.DateTime
import org.joda.time.Duration

import spire.algebra.Order
import spire.math.Rational

trait Plottable[T] {

  def isPlottable(t: T): Boolean = true
}

object Plottable {

  implicit val doublePlottable: Plottable[Double] =
    new Plottable[Double] {
      override def isPlottable(t: Double): Boolean = (!t.isInfinite) && (!t.isNaN)
    }

  implicit val longPlottable: Plottable[Long] = new Plottable[Long] {}

  implicit val intPlottable: Plottable[Int] = new Plottable[Int] {}

  implicit val dateTimePlottable: Plottable[DateTime] = new Plottable[DateTime] {}

  implicit val rationalPlottable: Plottable[Rational] =
    new Plottable[Rational] {
      override def isPlottable(t: Rational): Boolean = {
        val d = t.toDouble
        !isInfinite(d) && !isNaN(d)
      }
    }

  /*
  implicit val doublePlottable: Plottable[Double, Double] =
    new DoubleDoubleLengthSpace with Plottable[Double, Double] with DoubleTics {
      override def isPlottable(t: Double): Boolean = (!t.isInfinite) && (!t.isNaN)
    }

  implicit val longPlottable: Plottable[Long, Long] =
    new LongLongLengthSpace with Plottable[Long, Long] with LongTics

  implicit val intPlottable: Plottable[Int, Int] =
    new IntIntLengthSpace with Plottable[Int, Int] with IntTics

  implicit val dateTimePlottable: Plottable[DateTime, Duration] =
    new DateTimeDurationLengthSpace with axle.JodaDateTimeOrder with Plottable[DateTime, Duration] with DateTimeTics {

      lazy val now = new DateTime() // TODO !!!

      def zero: DateTime = now
    }
  
  implicit val rationalPlottable: Plottable[Rational, Rational] =
    new RationalRationalLengthSpace with Plottable[Rational, Rational] with RationalTics {

    lazy val or = Order[Rational]
    
    def compare(x: Rational, y: Rational): Int = or.compare(x, y)
    
    def zero: Rational = Rational(0)

    override def isPlottable(t: Rational): Boolean = {
      val d = t.toDouble
      !isInfinite(d) && !isNaN(d)
    }
  }
*/
  //  implicit def abstractAlgebraPlottable[N: Field: Order](implicit space: LengthSpace[N, Double]) =
  //    new Plottable[N, Double] {
  //
  //      val field = implicitly[Field[N]]
  //
  //      def zero: N = field.zero
  //    }

}
