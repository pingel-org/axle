package axle.stats

import org.specs2.mutable._

import axle.game.Dice._
import spire.math._

class ProbabilitySpec extends Specification {

  "coins" should {
    "work" in {

      val coin1 = coin()
      val coin2 = coin()
      P(coin1 is 'HEAD).apply() must be equalTo Rational(1, 2)
      P((coin1 is 'HEAD) and (coin2 is 'HEAD)).apply() must be equalTo Rational(1, 4)
      P((coin1 is 'HEAD) or (coin2 is 'HEAD)).apply() must be equalTo Rational(3, 4)
    }
  }

  "dice" should {
    "work" in {

      val d6a = die(6)
      val d6b = die(6)
      P(d6a is 1).apply() must be equalTo Rational(1, 6)
      P((d6a is 1) and (d6b is 2)).apply() must be equalTo Rational(1, 36)
      P(d6a isnt 3).apply() must be equalTo Rational(5, 6)
    }
  }

}
