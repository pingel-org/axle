package axle.joda

import axle.algebra.Tics
import org.specs2.mutable.Specification

import org.joda.time.DateTime
// import org.joda.time.Duration
// import org.joda.time.{ Seconds, Minutes, Hours, Days, Weeks }

class TicsSpec extends Specification {

  val start = new DateTime("2016-01-04T12:10:05.000-08:00")

  "Tics for Joda DateTime" should {
    "cover a month" in {

      val ts = Tics[DateTime].tics(start, start.plusMonths(1))

      ts must be equalTo List(
        (new DateTime("2016-01-11T12:10:05.000-08:00"), "01/11"),
        (new DateTime("2016-01-18T12:10:05.000-08:00"), "01/18"),
        (new DateTime("2016-01-25T12:10:05.000-08:00"), "01/25"),
        (new DateTime("2016-02-01T12:10:05.000-08:00"), "02/01"))
    }
  }

}