package axle.visualize

import math.{ pow, abs, log10, floor, ceil }

trait Plottable[T] extends Ordering[T] with Portionable[T] {

}

object Plottable {

  import org.joda.time.DateTime

  implicit object DoublePlottable extends Plottable[Double] {

    def compare(d1: Double, d2: Double) = (d1 - d2) match {
      case 0.0 => 0
      case r @ _ if r > 0.0 => 1
      case _ => -1
    }

    def portion(left: Double, v: Double, right: Double) = (v - left) / (right - left)

    def step(from: Double, to: Double): Double = pow(10, ceil(log10(abs(to - from))) - 1)

    def tics(from: Double, to: Double): Seq[(Double, String)] = {
      val s = step(from, to)
      val n = ceil((to - from) / s).toInt
      val start = BigDecimal.valueOf(s * floor(from / s))
      (0 to n).map(i => {
        val v = start + BigDecimal(s) * i
        (v.toDouble, v.toString)
      }).filter(vs => (vs._1 >= from && vs._1 <= to))
    }
  }

  implicit object LongPlottable extends Plottable[Long] {

    def compare(l1: Long, l2: Long) = (l1 - l2) match {
      case 0L => 0
      case r @ _ if r > 0L => 1
      case _ => -1
    }

    def portion(left: Long, v: Long, right: Long) = (v - left).toDouble / (right - left)

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

    def compare(i1: Int, i2: Int) = (i1 - i2) match {
      case 0 => 0
      case r @ _ if r > 0 => 1
      case _ => -1
    }

    def portion(left: Int, v: Int, right: Int) = (v - left).toDouble / (right - left)

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

    def compare(dt1: DateTime, dt2: DateTime) = dt1.compareTo(dt2)

    def portion(left: DateTime, v: DateTime, right: DateTime) = (v.getMillis - left.getMillis).toDouble / (right.getMillis - left.getMillis)

    def step(duration: Duration): (Duration, String) = {
      // TODO: bigger and smaller time-scales
      if (duration.isLongerThan(Days.ONE.toStandardDuration)) {
        (Days.ONE.toStandardDuration, "MM/dd hh")
      } else if (duration.isLongerThan(Hours.SEVEN.toStandardDuration)) {
        (Hours.TWO.toStandardDuration, "dd hh:mm")
      } else if (duration.isLongerThan(Hours.ONE.toStandardDuration)) {
        (Hours.ONE.toStandardDuration, "dd hh:mm")
      } else if (duration.isLongerThan(Minutes.THREE.toStandardDuration)) {
        (Minutes.ONE.toStandardDuration, "hh:mm")
      } else {
        (Seconds.ONE.toStandardDuration, "mm:ss")
      }
    }

    def tics(from: DateTime, to: DateTime): Seq[(DateTime, String)] = {
      val dur = new Interval(from, to).toDuration
      val (s, fmt) = step(dur)
      val n = dur.getMillis / s.getMillis
      (0L to n).map(i => {
        val d = from.plus(s.getMillis * i)
        (d, d.toString(fmt))
      })
    }
  }

  import axle.quanta.Information._

  case class InfoPlottable(base: UOM) extends Plottable[UOM] {

    def compare(u1: UOM, u2: UOM) = (u1.magnitudeIn(base).doubleValue - u2.magnitudeIn(base).doubleValue) match {
      case 0.0 => 0
      case r @ _ if r > 0.0 => 1
      case _ => -1
    }

    def portion(left: UOM, v: UOM, right: UOM) =
      (v.magnitudeIn(base).doubleValue - left.magnitudeIn(base).doubleValue) /
        (right.magnitudeIn(base).doubleValue - left.magnitudeIn(base).doubleValue)

    def step(from: Double, to: Double): Double = pow(10, ceil(log10(abs(to - from))) - 1)

    def tics(from: UOM, to: UOM): Seq[(UOM, String)] = {
      val fromD = from.magnitudeIn(base).doubleValue
      val toD = to.magnitudeIn(base).doubleValue
      val s = step(fromD, toD)
      val n = ceil((toD - fromD) / s).toInt
      val start = s * floor(fromD / s)
      val sD = new BigDecimal(s)
      (0 to n).map(i => {
        val v = start + sD * i
        (v.underlying *: base, v.toString)
      }) // TODO filter(vs => (vs._1 >= fromD && vs._1 <= toD))
    }

  }

}
