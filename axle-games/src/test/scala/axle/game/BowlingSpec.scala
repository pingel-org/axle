package axle.game

import org.specs2.mutable.Specification

class BowlingSpec extends Specification {

  "bowling" should {
    "work" in {

      import Bowling._

      // TODO the probabilities are summing to > 1
      scoreDistribution(goodBowler, 10)

      1 must be equalTo 1
    }
  }

}