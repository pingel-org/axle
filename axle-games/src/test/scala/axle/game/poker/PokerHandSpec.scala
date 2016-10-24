package axle.game.poker

import org.specs2.mutable._
import spire.compat.ordering
import spire.algebra.Eq

class PokerHandSpec extends Specification {

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

    "ace high < pair of 6s" in {
      val hand = PokerHand.fromString("3♡,6♢,9♠,T♡,A♡")
      hand.description must contain("high")
      hand must be lessThan PokerHand.fromString("6♡,6♢,T♠,J♠,4♡")
    }

    "straight > two pair" in {
      val hand = PokerHand.fromString("7♡,6♠,8♡,5♠,9♢")
      hand.description must contain("straight")
      hand must be greaterThan PokerHand.fromString("6♡,6♢,T♠,T♠,4♡")
    }

    "flush > two pair" in {
      val hand = PokerHand.fromString("3♡,6♡,9♡,T♡,A♡")
      hand.description must contain("flush")
      hand must be greaterThan PokerHand.fromString("6♡,6♢,T♠,T♠,4♡")
    }

    "straight flush > two pair" in {
      val hand = PokerHand.fromString("2♡,3♡,4♡,5♡,6♡")
      hand.description must contain("straight flush")
      hand must be greaterThan PokerHand.fromString("6♡,6♢,T♠,T♠,4♡")
    }

    "royal flush > two pair" in {
      val hand = PokerHand.fromString("T♡,J♡,Q♡,K♡,A♡")
      hand.description must contain("royal")
      hand must be greaterThan PokerHand.fromString("6♡,6♢,T♠,T♠,4♡")
    }

    "two pair vs two pair" in {
      val hand = PokerHand.fromString("6♡,6♢,T♠,T♡,A♡")
      hand.description must contain("two pair")
      hand must be lessThan PokerHand.fromString("6♡,6♢,T♠,J♠,J♡")
    }

    "full house vs two pair" in {
      val hand = PokerHand.fromString("6♡,6♢,T♠,T♡,T♢")
      hand.description must contain("full house")
      hand must be greaterThan PokerHand.fromString("6♡,6♢,T♠,J♠,J♡")
    }

    "pair vs pair" in {
      val hand = PokerHand.fromString("6♡,6♢,8♠,9♡,K♡")
      hand.description must contain("pair")
      hand must be lessThan PokerHand.fromString("K♡,K♢,2♠,3♠,5♡")
    }

    "three-of-a-kind vs three-of-a-kind" in {
      val hand = PokerHand.fromString("6♡,6♢,6♠,Q♡,K♡")
      hand.description must contain("three")
      hand must be lessThan PokerHand.fromString("7♡,7♢,7♠,3♠,4♡")
    }

    "four-of-a-kind vs four-of-a-kind" in {
      val hand = PokerHand.fromString("6♡,6♢,6♠,6♣,Q♡")
      hand.description must contain("four")
      hand must be lessThan PokerHand.fromString("7♡,7♢,7♠,7♣,2♡")
    }

  }

}