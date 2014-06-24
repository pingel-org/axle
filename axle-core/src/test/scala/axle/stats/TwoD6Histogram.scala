package axle.stats

import org.specs2.mutable.Specification

import axle.enrichGenSeq
import axle.game.Dice.die
import spire.implicits.IntAlgebra

object TwoD6Histogram extends Specification {

  "orderedTally" should {
    "work" in {

      val d6a = die(6)
      val d6b = die(6)

      val rolls = (0 until 1000).map(i => d6a.observe.get + d6b.observe.get)

      val hist = rolls.orderedTally

      hist.size must be equalTo 11
    }
  }

}