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

  "iffy (stochastic if)" should {
    "map fair boolean to d6 + (d6+d6)" in {

      import spire.implicits._
      import spire.algebra._

      val decision: Distribution0[Boolean, Rational] = binaryDecision(Rational(1, 3))

      val d6: Distribution0[Int, Rational] = die(6)

      val twoD6: Distribution0[Int, Rational] = for { a <- die(6); b <- die(6) } yield a + b

      val distribution = iffy(decision, d6, twoD6)

      distribution.probabilityOf(1) must be equalTo Rational(1, 18)
      distribution.probabilityOf(12) must be equalTo Rational(1, 54)
      distribution.values.map(v => distribution.probabilityOf(v)).reduce(implicitly[AdditiveMonoid[Rational]].plus) must be equalTo Rational(1)
    }
  }

}