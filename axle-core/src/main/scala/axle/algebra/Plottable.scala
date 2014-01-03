package axle.algebra

import spire.math._
import math.{ pow, abs, log10, floor, ceil }

trait Plottable[T] extends Ordering[T] with Portionable[T] {

  def isPlottable(t: T): Boolean

  def zero: T
}

object Plottable {

  import org.joda.time.DateTime

  implicit object DoublePlottable extends Plottable[Double] {

    def isPlottable(t: Double): Boolean = (!t.isInfinite) && (!t.isNaN)

    def zero: Double = 0d

    def compare(d1: Double, d2: Double): Int = (d1 - d2) match {
      case 0d => 0
      case r @ _ if r > 0d => 1
      case _ => -1
    }

    def portion(left: Double, v: Double, right: Double): Double = (v - left) / (right - left)

    def step(from: Double, to: Double): Double = pow(10, ceil(log10(abs(to - from))) - 1)

    def tics(from: Double, to: Double): Seq[(Double, String)] = {
      if (from.isNaN || from.isInfinity || to.isNaN || to.isInfinity) {
        List((0d, "0.0"), (1d, "1.0"))
      } else {
        val s = step(from, to)
        val n = ceil((to - from) / s).toInt
        val start = BigDecimal.valueOf(s * floor(from / s))
        (0 to n).map(i => {
          val v = start + BigDecimal(s) * i
          (v.toDouble, v.toString)
        }).filter(vs => (vs._1 >= from && vs._1 <= to))
      }
    }
  }

  implicit object LongPlottable extends Plottable[Long] {

    def isPlottable(t: Long): Boolean = true

    def zero: Long = 0L

    def compare(l1: Long, l2: Long): Int = (l1 - l2) match {
      case 0L => 0
      case r @ _ if r > 0L => 1
      case _ => -1
    }

    def portion(left: Long, v: Long, right: Long): Double = (v - left).toDouble / (right - left)

    def step(from: Long, to: Long): Long = max(1, pow(10, ceil(log10(abs(to - from))) - 1).toLong)

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

  implicit object IntPlottable extends Plottable[Int] {

    def isPlottable(t: Int): Boolean = true

    def zero: Int = 0

    def compare(i1: Int, i2: Int): Int = (i1 - i2) match {
      case 0 => 0
      case r @ _ if r > 0 => 1
      case _ => -1
    }

    def portion(left: Int, v: Int, right: Int): Double = (v - left).toDouble / (right - left)

    def step(from: Int, to: Int): Int = max(1, pow(10, ceil(log10(abs(to - from))) - 1).toInt)

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

  implicit object DateTimePlottable extends Plottable[DateTime] {

    import org.joda.time._

    def isPlottable(t: DateTime): Boolean = true

    lazy val now = new DateTime()

    def zero: DateTime = now

    def compare(dt1: DateTime, dt2: DateTime): Int = dt1.compareTo(dt2)

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

}
