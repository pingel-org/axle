package axle.game

import org.scalatest._
import spire.math.Rational

class OldMontyHallSpec extends FunSuite with Matchers {

  test("Monty Hall contestant should always pick the other door") {

    import OldMontyHall._

    chanceOfWinning(Rational(1)) should be(Rational(1, 2))

    chanceOfWinning(Rational(0)) should be(Rational(1, 3))

    // TODO: p1 > p2 <=> chanceOfWinning(p1) > chanceOfWinning(p2)
    //        aka "is monotonically increasing"
  }

}
