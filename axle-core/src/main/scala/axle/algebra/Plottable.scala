package axle.algebra

import spire.math._
import spire.implicits._
import spire.algebra.Eq
import spire.algebra.Field
import spire.algebra.Order
import spire.algebra.MetricSpace
import spire.algebra.Monoid
import spire.algebra.AdditiveMonoid
import java.lang.Double.{ isInfinite, isNaN }
import math.{ pow, abs, log10, floor, ceil }

trait Plottable[T] extends Order[T] {

  def zero: T

  def portion(left: T, v: T, right: T): Double

  def tics(from: T, to: T): Seq[(T, String)]

  def isPlottable(t: T): Boolean
}

object Plottable {

  import org.joda.time.DateTime

  implicit object DoublePlottable extends spire.std.DoubleAlgebra with Plottable[Double] {

    def isPlottable(t: Double): Boolean = (!t.isInfinite) && (!t.isNaN)

    def portion(left: Double, v: Double, right: Double): Double = (v - left) / (right - left)

    def step(from: Double, to: Double): Double = pow(10, ceil(log10(abs(to - from))).toInt - 1)

    def tics(from: Double, to: Double): Seq[(Double, String)] = {
      if ((from === to) || from.isNaN || from.isInfinity || to.isNaN || to.isInfinity) {
        List((0d, "0.0"), (1d, "1.0"))
      } else {
        val s = step(from, to)
        val n = ceil((to - from) / s).toInt
        val start = BigDecimal.valueOf(s * floor(from / s))
        (0 to n).map(i => {
          val v = start + BigDecimal(s) * i
          (v.toDouble, v.toString)
        }).filter({ case (d, _) => (d >= from && d <= to) })
      }
    }
  }

  val asdf = implicitly[Order[Long]]

  implicit object LongPlottable extends spire.std.LongAlgebra with Plottable[Long] {

    def isPlottable(t: Long): Boolean = true

    def portion(left: Long, v: Long, right: Long): Double = (v - left).toDouble / (right - left)

    def step(from: Long, to: Long): Long = {
      val n = (scala.math.ceil(scala.math.log10(abs(to - from))) - 1).toInt
      max(1, pow(10, n).toLong)
    }

    def tics(from: Long, to: Long): Seq[(Long, String)] = {
      val s = step(from, to)
      val n = (to - from) / s
      val start = (s * (from / s))
      (0L to n).map(i => {
        val v = start + s * i
        (v, v.toString)
      }).filter(vs => (vs._1 >= from && vs._1 <= to))
    }

  }

  implicit object IntPlottable extends spire.std.IntAlgebra with Plottable[Int] {

    def isPlottable(t: Int): Boolean = true

    def portion(left: Int, v: Int, right: Int): Double = (v - left).toDouble / (right - left)

    def step(from: Int, to: Int): Int = {
      val n = (scala.math.ceil(scala.math.log10(abs(to - from))) - 1).toInt
      max(1, pow(10, n).toInt)
    }

    def tics(from: Int, to: Int): Seq[(Int, String)] = {
      val s = step(from, to)
      val n = (to - from) / s
      val start = (s * (from / s))
      (0 to n).map(i => {
        val v = start + s * i
        (v, v.toString)
      }).filter(vs => (vs._1 >= from && vs._1 <= to))
    }
  }

  implicit object DateTimePlottable extends axle.JodaDateTimeOrder with Plottable[DateTime] {

    import org.joda.time._

    def isPlottable(t: DateTime): Boolean = true

    lazy val now = new DateTime()

    def zero: DateTime = now

    def portion(left: DateTime, v: DateTime, right: DateTime): Double = (v.getMillis - left.getMillis).toDouble / (right.getMillis - left.getMillis)

    // TODO: bigger and smaller time-scales
    def step(duration: Duration): (DateTime => DateTime, String) =
      if (duration.isLongerThan(Weeks.ONE.multipliedBy(104).toStandardDuration)) {
        (_.plusMonths(6), "MM/dd YY")
      } else if (duration.isLongerThan(Weeks.ONE.multipliedBy(20).toStandardDuration)) {
        (_.plusMonths(1), "MM/dd YY")
      } else if (duration.isLongerThan(Weeks.THREE.toStandardDuration)) {
        (_.plusWeeks(1), "MM/dd")
      } else if (duration.isLongerThan(Days.ONE.toStandardDuration)) {
        (_.plusDays(1), "MM/dd hh")
      } else if (duration.isLongerThan(Hours.SEVEN.toStandardDuration)) {
        (_.plusHours(2), "dd hh:mm")
      } else if (duration.isLongerThan(Hours.ONE.toStandardDuration)) {
        (_.plusHours(1), "dd hh:mm")
      } else if (duration.isLongerThan(Minutes.TWO.toStandardDuration)) {
        (_.plusMinutes(1), "hh:mm")
      } else if (duration.isLongerThan(Seconds.ONE.multipliedBy(10).toStandardDuration())) {
        (_.plusSeconds(10), "mm:ss")
      } else {
        (_.plusSeconds(1), "mm:ss")
      }

    def ticStream(from: DateTime, to: DateTime, stepFn: DateTime => DateTime, fmt: String): Stream[(DateTime, String)] = {
      val nextTic = stepFn(from)
      if (nextTic.isAfter(to)) {
        Stream.empty
      } else {
        Stream.cons((nextTic, nextTic.toString(fmt)), ticStream(nextTic, to, stepFn, fmt))
      }
    }

    def tics(from: DateTime, to: DateTime): Seq[(DateTime, String)] = {
      val dur = new Interval(from, to).toDuration
      val (stepFn, fmt) = step(dur)
      ticStream(from, to, stepFn, fmt).toList
    }

  }

  implicit object RationalPlottable extends Plottable[Rational] {

    val ratOrd = implicitly[Order[Rational]]

    def zero: Rational = Rational(0)
    
    def compare(x: Rational, y: Rational): Int = ratOrd.compare(x, y) 
    
    def isPlottable(t: Rational): Boolean = {
      val d = t.toDouble
      !isInfinite(d) && !isNaN(d)
    }

    def portion(left: Rational, v: Rational, right: Rational): Double =
      ((v - left) / (right - left)).toDouble

    import spire.implicits._

    def step(from: Rational, to: Rational): Rational = {
      val power = (ceil(log10((to - from).abs.toDouble)) - 1).toInt
      if (power >= 0) {
        Rational(10 ** power, 1)
      } else {
        // spire doesn't like negative arguments to **
        Rational(1, 10 ** power.abs)
      }
    }

    def tics(from: Rational, to: Rational): Seq[(Rational, String)] = {
      val fromDouble = from.toDouble
      val toDouble = to.toDouble
      if (isNaN(fromDouble) || isInfinite(fromDouble) || isNaN(toDouble) || isInfinite(toDouble)) {
        List((Rational.zero, "0"), (Rational(1), "1"))
      } else {
        val s = step(from, to)
        val start = (from / s).floor * s
        val n = ((to - from) / s).ceil.toInt
        (0 to n).map(i => {
          val v = start + s * i
          (v, v.toString)
        }).filter({ case (d, _) => (d >= from && d <= to) })
      }
    }
  }

  implicit def abstractAlgebraPlottable[N: Field: Order](implicit space: MetricSpace[N, Double]) = new Plottable[N] {

    import spire.algebra._
    import spire.implicits._

    val ord = implicitly[Order[N]]
    val field = implicitly[Field[N]]

    def zero: N = field.zero
    
    def compare(x: N, y: N): Int = ord.compare(x, y)

    def isPlottable(t: N): Boolean = true

    def portion(left: N, v: N, right: N): Double =
      space.distance(v, right) / space.distance(right, left)

    // TODO
    def tics(from: N, to: N): Seq[(N, String)] =
      Vector((from, from.toString), (to, to.toString))

  }

}
