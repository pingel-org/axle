package axle.stats

import spire.math.Rational
import org.specs2.mutable._

object DistributionSpec extends Specification {

  "Distribution map" should {
    "work" in {

      val c = ConditionalProbabilityTable0(Map(
        List(1, 2, 3) -> Rational(1, 3),
        List(1, 2, 8) -> Rational(1, 2),
        List(8, 9) -> Rational(1, 6)))

      val distSize = c.map(_.size)

      distSize.probabilityOf(3) must be equalTo Rational(5, 6)
    }
  }

}