package axle.game.poker

import org.specs2.mutable._

import axle.game.cards._
import spire.algebra.Eq
import spire.compat.ordering

class PokerSpec extends Specification {

  "poker hand ranking" should {

    "work" in {

      val shared = PokerHand.fromString("J♡,T♠,6♡,6♢,8♡")
      val personals = Vector("J♠,4♠", "A♠,T♢", "K♠,Q♢").map(PokerHand.fromString)

      val hands = personals map { personal =>
        (personal.cards ++ shared.cards).combinations(5).map(PokerHand(_)).max
      }

      val jacksAndSixes = PokerHand.fromString("6♡,6♢,T♠,J♠,J♡")

      true must be equalTo Eq[PokerHand].eqv(hands.max, jacksAndSixes)
    }
  }

  "poker hand comparison" should {

    "work for 2 pair" in {
      PokerHand.fromString("6♡,6♢,T♠,T♡,A♡") must be lessThan PokerHand.fromString("6♡,6♢,T♠,J♠,J♡")
    }

    "work for pair" in {
      PokerHand.fromString("6♡,6♢,8♠,9♡,K♡") must be lessThan PokerHand.fromString("K♡,K♢,2♠,3♠,5♡")
    }

    "work for three-of-a-kind" in {
      PokerHand.fromString("6♡,6♢,6♠,Q♡,K♡") must be lessThan PokerHand.fromString("7♡,7♢,7♠,3♠,4♡")
    }

    "work for four-of-a-kind" in {
      PokerHand.fromString("6♡,6♢,6♠,6♣,Q♡") must be lessThan PokerHand.fromString("7♡,7♢,7♠,7♣,2♡")
    }

  }

}
