package axle.game

import org.scalatest._

import axle.game.cards.Card

class CardSpec extends FunSuite with Matchers {

  test("cards serialize") {
    Card("6♡").serialize should be("6♡")
  }

  test("cards equal") {
    import cats.kernel.Eq
    Eq[Card].eqv(Card("7♣"), Card("7♣")) should be(true)
  }

}
