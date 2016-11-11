package axle.joda

import org.specs2.mutable.Specification

import org.joda.time.DateTime
import org.joda.time.Duration

class LengthSpaceSpec extends Specification {

  val start = new DateTime("2016-01-04T12:10:05.000-08:00")

  "joda LengthSpace" should {
    "compute time onPath" in {

      val midpoint = dateTimeDurationLengthSpace.onPath(start, start.plusDays(1), 0.5)
      midpoint must be equalTo start.plusHours(12)
    }

    "compute day distance" in {

      val distance = dateTimeDurationLengthSpace.distance(start, start.plusDays(1))
      distance must be equalTo Duration.standardDays(1)
    }
  }

  "joda Eq" should {
    "distinguish different times" in {
      axle.joda.dateTimeEq.eqv(start, start.plusHours(1)) must be equalTo false
    }
  }
}
