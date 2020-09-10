package axle.game

import org.scalatest.funsuite._
import org.scalatest.matchers.should.Matchers
import cats.implicits._
import spire.math.Rational
import axle.probability._
import axle.algebra.RegionEq
import axle.syntax.kolmogorov._

class BowlingSpec extends AnyFunSuite with Matchers {
  
  test("bowling") {

    import Bowling._
    import Bowlers._

    val stateD: CPTR[Bowling.State] = stateDistribution(goodBowler, 4)

    val scoreD = stateD.map(_.tallied)

    // TODO: make same assertion about P(300) when last frame is handled correctly
    scoreD.P(RegionEq(0)) should be > Rational(0)
  }
}
