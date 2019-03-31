package axle.game

import org.scalatest._
import cats.implicits._
import spire.math._
import axle.stats._

class OldMontyHallHalfIsKolmogorov
  extends KolmogorovProbabilitySpec(
    "Monty Hall outcome(1/2)",
    { import OldMontyHall._; outcome(Rational(1, 2))}
  )

class OldMontyHallQuarterIsKolmogorov
  extends KolmogorovProbabilitySpec(
    "Monty Hall outcome(1/4)",
    { import OldMontyHall._; outcome(Rational(1, 4))}
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
