package axle.game

import org.specs2.mutable.Specification
import spire.math.Rational

class MontyHallSpec extends Specification {

  "Monty Hall contestant" should {

    "always pick the other door" in {

      import MontyHall._

      chanceOfWinning(Rational(1)) must be equalTo Rational(1, 2)

      chanceOfWinning(Rational(0)) must be equalTo Rational(1, 3)

      // TODO: p1 > p2 <=> chanceOfWinning(p1) > chanceOfWinning(p2)
      //        aka "is monotonically increasing"
    }
  }

}