package axle.game

import org.scalatest._
import org.scalacheck.Arbitrary
import org.scalacheck.Gen
import cats.implicits._
import spire.math._
import axle.stats._

class OldMontyHallHalfIsKolmogorov
  extends KolmogorovProbabilityProperties(
    "Monty Hall with arbitrary switch probability",
    Arbitrary({ import OldMontyHall._
      Gen.choose(0d,1d).map(Rational.apply).map(outcome)
    })
  )

class OldMontyHallSpec extends FunSuite with Matchers {

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
