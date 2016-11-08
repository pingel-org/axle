package axle.game

import org.specs2.mutable.Specification

class BowlingSpec extends Specification {

  "bowling" should {
    "work" in {

      import Bowling._
      import Bowlers._

      val stateD = stateDistribution(goodBowler, 4)

      val scoreD = stateD.map(_.tallied)

      scoreD.probabilityOf(0) must be greaterThan 0
      // TODO: make same assertion about P(300) when last frame is handled correctly
    }
  }
  
}
