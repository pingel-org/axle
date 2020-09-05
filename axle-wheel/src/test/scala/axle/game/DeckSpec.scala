package axle.game

import org.scalatest.funsuite._
import org.scalatest.matchers.should.Matchers
import axle.game.cards.Deck
import cats.implicits._

class DeckSpec extends AnyFunSuite with Matchers {

  test("deck has a string representation") {
    Deck().show.length should be(52 * 3 - 1)
  }

}
