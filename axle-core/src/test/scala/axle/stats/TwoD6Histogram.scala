package axle.stats

import org.specs2.mutable.Specification

import axle.game.Dice.die
import axle.enrichGenSeq
import spire.math.Rational
import spire.implicits.IntAlgebra

object TwoD6Histogram extends Specification {

  "tally" should {
    "work" in {

      val d6a = die(6)
      val d6b = die(6)
      val rolls = (0 until 1000) map { i => d6a.observe + d6b.observe }

      val hist = rolls.tally
      hist.size must be equalTo 11
    }
  }

  "distribution monad" should {
    "combine 2 D6 correctly" in {

      val dice = for {
        a <- die(6)
        b <- die(6)
      } yield a + b

      dice.probabilityOf(2) must be equalTo Rational(1, 36)
      dice.probabilityOf(7) must be equalTo Rational(1, 6)
      dice.probabilityOf(12) must be equalTo Rational(1, 36)
    }

  }

}