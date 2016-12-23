package axle.game

import org.scalatest._
import axle.game.cards.Deck
import axle.string

class DeckSpec extends FunSuite with Matchers {

  test("deck has a string representation") {
    string(Deck()).length should be(52 * 3 - 1)
  }

}
