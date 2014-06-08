package axle.stats

import org.specs2.mutable._
import axle._
import axle.game.Dice._
import spire.math._
import spire.implicits._
import spire.algebra._

object TwoD6Histogram extends Specification {

  "orderedTally" should {
    "work" in {

      val d6a = die(6)
      val d6b = die(6)

      val rolls = (0 until 1000).map(i => d6a.observe.get + d6b.observe.get)

      implicitly[Ring[Int]] // TODO this is here to prevent diverging expansion. to be diagnosed

      val hist = rolls.orderedTally

      hist.size must be equalTo 11
      
      1 must be equalTo 1
    }
  }

}