package axle.game

import org.scalatest.funsuite._
import org.scalatest.matchers.should.Matchers
import org.scalacheck.Arbitrary
import org.scalacheck.Gen
import cats.implicits._
import spire.math._
import axle.stats._
import axle.algebra.Region
import axle.algebra.RegionEq

class OldMontyHallHalfIsKolmogorov
  extends KolmogorovProbabilityProperties[Rational, ConditionalProbabilityTable, Boolean, Rational](
    "Monty Hall with arbitrary switch probability",
    Arbitrary({ Gen.choose(0d,1d).map(Rational.apply) }),
    { import OldMontyHall._; switchProb => outcome(switchProb) },
    switchProb => Arbitrary(Gen.oneOf(List(true, false).map(RegionEq(_)))), // TODO full range of Regions (not just RegionEq)
    switchProb => Region.eqRegionIterable(List(true, false))
  )

class OldMontyHallSpec extends AnyFunSuite with Matchers {

  test("Monty Hall contestant should always pick the other door") {

    import OldMontyHall._

    // 2/3 chance of winning by switching
    chanceOfWinning(Rational(1)) should be(Rational(2, 3))

    // 1/3 chance of winning by staying
    chanceOfWinning(Rational(0)) should be(Rational(1, 3))

    // TODO: p1 > p2 <=> chanceOfWinning(p1) > chanceOfWinning(p2)
    //        aka "is monotonically increasing"
  }

}
