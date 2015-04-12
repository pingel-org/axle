
package axle

import spire.algebra.Order
import spire.algebra.Eq

import axle.algebra.Zero
import axle.algebra.Tics
import axle.algebra.Plottable
import axle.algebra.LengthSpace

import org.joda.time.DateTime
import org.joda.time.Duration
import org.joda.time.{Seconds, Minutes, Hours, Days, Weeks}

package object joda {

  def dateTimeZero(t: DateTime): Zero[DateTime] = new Zero[DateTime] {
    def zero: DateTime = t // new DateTime()
  }

  trait JodaDateTimeOrder extends Order[DateTime] {
    def compare(dt1: DateTime, dt2: DateTime): Int = dt1.compareTo(dt2)
  }

  implicit val dateTimeOrder: Order[DateTime] = new JodaDateTimeOrder {}

  implicit val dateTimeOrdering = spire.compat.ordering(dateTimeOrder)

  trait JodaDateTimeEq extends Eq[DateTime] {
    def eqv(x: DateTime, y: DateTime): Boolean = x.equals(y)
  }

  implicit val dateTimePlottable: Plottable[DateTime] = new Plottable[DateTime] {}

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

  trait DateTimeDurationLengthSpace extends LengthSpace[DateTime, Duration] {

    def distance(v: DateTime, w: DateTime): Duration = new Duration(v, w)

    def onPath(left: DateTime, right: DateTime, p: Double): DateTime = left.plusMillis(((right.getMillis - left.getMillis).toDouble * p).toInt)

    def portion(left: DateTime, v: DateTime, right: DateTime): Double = (v.getMillis - left.getMillis).toDouble / (right.getMillis - left.getMillis)

  }

}
