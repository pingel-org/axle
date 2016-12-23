package axle.stats

import axle.spireToCatsOrder
import spire.math.Rational
import org.scalatest._

class DistributionSpec extends FunSuite with Matchers {

  test("Distribution map") {

    val c = ConditionalProbabilityTable0(Map(
      List(1, 2, 3) -> Rational(1, 3),
      List(1, 2, 8) -> Rational(1, 2),
      List(8, 9) -> Rational(1, 6)))

    val distSize = c.map(_.size)

    distSize.probabilityOf(3) should be(Rational(5, 6))
  }

}
