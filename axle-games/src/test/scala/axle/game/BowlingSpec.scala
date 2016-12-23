package axle.game

import org.scalatest._
import spire.math.Rational

class BowlingSpec extends FunSuite with Matchers {

  test("bowling") {

    import Bowling._
    import Bowlers._

    val stateD = stateDistribution(goodBowler, 4)

    val scoreD = stateD.map(_.tallied)

    // TODO: make same assertion about P(300) when last frame is handled correctly
    scoreD.probabilityOf(0) should be > Rational(0)
  }
}
