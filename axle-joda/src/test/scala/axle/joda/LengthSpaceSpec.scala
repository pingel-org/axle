package axle.joda

import org.specs2.mutable.Specification

import org.joda.time.DateTime

class LengthSpaceSpec extends Specification {

  val start = new DateTime("2016-01-04T12:10:05.000-08:00")

  "joda LengthSpace" should {
    "compute time onPath" in {

      val midpoint = dateTimeDurationLengthSpace.onPath(start, start.plusDays(1), 0.5)
      midpoint must be equalTo start.plusHours(12)
    }
  }
}