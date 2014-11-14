package axle.algebra

import spire.math._
import spire.algebra._
import spire.implicits._
import axle.string
import axle.Show.showDouble
import org.joda.time.DateTime
import org.joda.time._
import java.lang.Double.{ isInfinite, isNaN }

trait Tics[T] {

  def tics(from: T, to: T): Seq[(T, String)]

}

trait DoubleTics extends Tics[Double] {

  def step(from: Double, to: Double): Double = pow(10, ceil(log10(abs(to - from))) - 1)

  def tics(from: Double, to: Double): Seq[(Double, String)] = {
    if ((from === to) || from.isNaN || from.isInfinity || to.isNaN || to.isInfinity) {
      List((0d, "0.0"), (1d, "1.0"))
    } else {
      val s = step(from, to)
      val n = ceil((to - from) / s).toInt
      val w = s * floor(from / s)
      val start = BigDecimal.valueOf(w)
      (0 to n).map(i => {
        val v = start + BigDecimal(s) * i
        (v.toDouble, string(v))
      }).filter({ case (d, _) => (d >= from && d <= to) })
    }
  }

}

trait LongTics extends Tics[Long] {

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
      (v, string(v))
    }).filter(vs => (vs._1 >= from && vs._1 <= to))
  }
}

trait IntTics extends Tics[Int] {

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
      (v, string(v))
    }).filter(vs => (vs._1 >= from && vs._1 <= to))
  }

}

trait DateTimeTics extends Tics[DateTime] {

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
    val dur = new org.joda.time.Interval(from, to).toDuration
    val (stepFn, fmt) = step(dur)
    ticStream(from, to, stepFn, fmt).toList
  }

}

trait RationalTics extends Tics[Rational] {

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
        (v, string(v))
      }).filter({ case (d, _) => (d >= from && d <= to) })
    }
  }

}

//object Tics {
//  implicit def abstractAlgebraTics[N: Field: Order]: Tics[N] = new Tics[N] {
//
//    // TODO
//    def tics(from: N, to: N): Seq[(N, String)] =
//      Vector((from, from.toString), (to, to.toString))
//
//  }
//
//}
