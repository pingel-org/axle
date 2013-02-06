package axle.visualize

import axle.algebra.Portionable
import math.{ pow, abs, log10, floor, ceil }

trait Plottable[T] extends Ordering[T] with Portionable[T] {

  def isPlottable(t: T): Boolean

  def zero(): T
}

object Plottable {

  import org.joda.time.DateTime

  implicit object DoublePlottable extends Plottable[Double] {

    def isPlottable(t: Double): Boolean = (!t.isInfinite) && (!t.isNaN)

    def zero() = 0.0

    def compare(d1: Double, d2: Double) = (d1 - d2) match {
      case 0.0 => 0
      case r @ _ if r > 0.0 => 1
      case _ => -1
    }

    def portion(left: Double, v: Double, right: Double) = (v - left) / (right - left)

    def step(from: Double, to: Double): Double = pow(10, ceil(log10(abs(to - from))) - 1)

    def tics(from: Double, to: Double): Seq[(Double, String)] = {
      if (from.isNaN || from.isInfinity || to.isNaN || to.isInfinity) {
        List((0.0, "0.0"), (1.0, "1.0"))
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

    def zero() = 0L

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

    def isPlottable(t: Int): Boolean = true

    def zero() = 0

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

    def isPlottable(t: DateTime): Boolean = true

    lazy val now = new DateTime()

    def zero() = now

    def compare(dt1: DateTime, dt2: DateTime) = dt1.compareTo(dt2)

    def portion(left: DateTime, v: DateTime, right: DateTime) = (v.getMillis - left.getMillis).toDouble / (right.getMillis - left.getMillis)

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
      } else if (duration.isLongerThan(Minutes.THREE.toStandardDuration)) {
        (_.plusMinutes(1), "hh:mm")
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

  //  import axle.quanta.Information._
  //
  //  case class InfoPlottable(base: Q) extends Plottable[Q] {
  //
  //    def isPlottable(t: Q): Boolean = true
  //
  //    def zero() = 0.0 *: bit
  //
  //    def compare(u1: Q, u2: Q) = ((u1 in base).magnitude.doubleValue - (u2 in base).magnitude.doubleValue) match {
  //      case 0.0 => 0
  //      case r @ _ if r > 0.0 => 1
  //      case _ => -1
  //    }
  //
  //    def portion(left: Q, v: Q, right: Q) =
  //      ((v in base).magnitude.doubleValue - (left in base).magnitude.doubleValue) /
  //        ((right in base).magnitude.doubleValue - (left in base).magnitude.doubleValue)
  //
  //    def step(from: Double, to: Double): Double = pow(10, ceil(log10(abs(to - from))) - 1)
  //
  //    def tics(from: Q, to: Q): Seq[(Q, String)] = {
  //      val fromD = (from in base).magnitude.doubleValue
  //      val toD = (to in base).magnitude.doubleValue
  //      val s = step(fromD, toD)
  //      val n = ceil((toD - fromD) / s).toInt
  //      val start = s * floor(fromD / s)
  //      val sD = new BigDecimal(s)
  //      (0 to n).map(i => {
  //        val v = start + sD * i
  //        (v.underlying *: base, v.toString)
  //      }) // TODO filter(vs => (vs._1 >= fromD && vs._1 <= toD))
  //    }
  //
  //  }

  import axle.quanta._

  case class QuantumPlottable(quantum: Quantum) {

    def withBase(base: quantum.Q) = UnitPlottable(base)
    case class UnitPlottable(base: quantum.Q) extends Plottable[quantum.Q] {

      def isPlottable(t: quantum.Q): Boolean = true

      def zero() = 0.0 *: base

      def compare(u1: quantum.Q, u2: quantum.Q) =
        ((u1 in base).magnitude.doubleValue - (u2 in base).magnitude.doubleValue) match {
          case 0.0 => 0
          case r @ _ if r > 0.0 => 1
          case _ => -1
        }

      def portion(left: quantum.Q, v: quantum.Q, right: quantum.Q) =
        ((v in base).magnitude.doubleValue - (left in base).magnitude.doubleValue) /
          ((right in base).magnitude.doubleValue - (left in base).magnitude.doubleValue)

      def step(from: Double, to: Double): Double = pow(10, ceil(log10(abs(to - from))) - 1)

      def tics(from: quantum.Q, to: quantum.Q): Seq[(quantum.Q, String)] = {
        val fromD = (from in base).magnitude.doubleValue
        val toD = (to in base).magnitude.doubleValue
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

}
