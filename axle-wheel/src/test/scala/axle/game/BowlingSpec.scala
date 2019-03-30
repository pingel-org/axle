package axle.game

import org.scalatest._
import cats.syntax.all._
import spire.math.Rational
import axle.stats._
import axle.syntax.probabilitymodel._

class BowlingSpec extends FunSuite with Matchers {

  test("bowling") {

    import Bowling._
    import Bowlers._

    val stateD: F[State] = stateDistribution(goodBowler, 4)

    val scoreD: F[Int] = for {
      state <- stateD
    } yield state.tallied

    // TODO: make same assertion about P(300) when last frame is handled correctly
    scoreD.P(0) should be > Rational(0)
  }
}
