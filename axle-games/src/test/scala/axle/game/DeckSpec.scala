package axle.game

import org.specs2.mutable._
import axle.game.cards.Deck
import axle.string

class DeckSpec extends Specification {

  "deck" should {
    "have a string representation" in {
      string(Deck()).length must be equalTo (52 * 3 - 1)
    }
  }

}
