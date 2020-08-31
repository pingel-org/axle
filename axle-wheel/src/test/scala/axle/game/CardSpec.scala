package axle.game

import org.scalatest.funsuite._
import org.scalatest.matchers.should.Matchers

import axle.game.cards.Card

class CardSpec extends AnyFunSuite with Matchers {

  test("cards serialize") {
    Card("6♡").serialize should be("6♡")
  }

  test("cards equal") {
    import cats.kernel.Eq
    Eq[Card].eqv(Card("7♣"), Card("7♣")) should be(true)
  }

}
