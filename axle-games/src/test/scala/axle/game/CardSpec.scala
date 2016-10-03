package axle.game

import org.specs2.mutable._

import axle.game.cards.Card

class CardSpec extends Specification {

  "cards" should {
    "serialize" in {
      Card("6♡").serialize must be equalTo "6♡"
    }
    "be equal" in {
      import spire.algebra.Eq
      Eq[Card].eqv(Card("7♣"), Card("7♣")) must be equalTo true
    }
  }

}