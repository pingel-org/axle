package axle.stats

import org.scalatest._

import axle.enrichGenSeq
import axle.game.Dice.die
import spire.implicits.IntAlgebra
import spire.math.Rational

object TwoD6Histogram extends FunSuite with Matchers {

  test("tally") {

    val d6a = die(6)
    val d6b = die(6)
    val rolls = (0 until 1000) map { i => d6a.observe + d6b.observe }

    val hist = rolls.tally
    hist.size should be(11)
  }

  test("distribution monad: combine 2 D6 correctly") {

    val dice = for {
      a <- die(6)
      b <- die(6)
    } yield a + b

    dice.probabilityOf(2) should be(Rational(1, 36))
    dice.probabilityOf(7) should be(Rational(1, 6))
    dice.probabilityOf(12) should be(Rational(1, 36))
  }

}
