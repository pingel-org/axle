package axle.game.poker

import org.specs2.mutable._

import axle._
import axle.game.cards._
import spire.algebra.Eq
import spire.compat.ordering

class PokerSpec extends Specification {

  def string2cards(s: String) = s.split(",").map(Card(_))

  def string2hand(s: String): PokerHand = PokerHand(string2cards(s))

  "poker hand ranking" should {

    "work" in {

      val shared = string2cards("J♡,T♠,6♡,6♢,8♡")
      val personals = Vector("J♠,4♠","A♠,T♢","K♠,Q♢").map(string2cards)

      val hands = personals map { personal =>
        (personal ++ shared).combinations(5).map(PokerHand(_)).max
      }

      val jacksAndSixes = string2hand("6♡,6♢,T♠,J♠,J♡")

      true must be equalTo Eq[PokerHand].eqv(hands.max, jacksAndSixes)
    }
  }

  "poker hand comparison" should {

    "work for 2 pair" in {
      string2hand("6♡,6♢,T♠,T♡,A♡") must be lessThan string2hand("6♡,6♢,T♠,J♠,J♡")
    }

    "work for pair" in {
      string2hand("6♡,6♢,8♠,9♡,K♡") must be lessThan string2hand("K♡,K♢,2♠,3♠,5♡")
    }

    "work for three-of-a-kind" in {
      string2hand("6♡,6♢,6♠,Q♡,K♡") must be lessThan string2hand("7♡,7♢,7♠,3♠,4♡")
    }

    "work for four-of-a-kind" in {
      string2hand("6♡,6♢,6♠,6♣,Q♡") must be lessThan string2hand("7♡,7♢,7♠,7♣,2♡")
    }

  }

}
