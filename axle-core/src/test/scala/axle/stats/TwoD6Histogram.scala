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
      val rolls = (0 until 1000).map(i => d6a.observe + d6b.observe)

      val hist = rolls.tally
      hist.size must be equalTo 11
    }
  }

  "distribution monad" should {
    "combined 2 D6 correctly" in {

      val twoD6 = for {
        a <- die(6).distribution
        b <- die(6).distribution
      } yield a + b

      twoD6.probabilityOf(2) must be equalTo Rational(1, 36)
      twoD6.probabilityOf(7) must be equalTo Rational(1, 6)
      twoD6.probabilityOf(12) must be equalTo Rational(1, 36)
    }

  }

}