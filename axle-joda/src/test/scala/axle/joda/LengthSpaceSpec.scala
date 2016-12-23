package axle.joda

import org.scalatest._

import org.joda.time.DateTime
import org.joda.time.Duration

class LengthSpaceSpec extends FunSuite with Matchers {

  val start = new DateTime("2016-01-04T12:10:05.000-08:00")

  test("joda LengthSpace compute time onPath") {

    val midpoint = dateTimeDurationLengthSpace.onPath(start, start.plusDays(1), 0.5)
    midpoint should be(start.plusHours(12))
  }

  test("joda LengthSpace compute day distance") {

    val distance = dateTimeDurationLengthSpace.distance(start, start.plusDays(1))
    distance should be(Duration.standardDays(1))
  }

  test("joda Eq") {
    axle.joda.dateTimeEq.eqv(start, start.plusHours(1)) should be(false)
  }
}
