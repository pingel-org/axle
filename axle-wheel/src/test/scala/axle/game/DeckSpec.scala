package axle.game

import org.scalatest._
import axle.game.cards.Deck
import cats.implicits._

class DeckSpec extends FunSuite with Matchers {

  test("deck has a string representation") {
    Deck().show.length should be(52 * 3 - 1)
  }

}
