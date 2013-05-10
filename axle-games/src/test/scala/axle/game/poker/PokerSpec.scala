package axle.game.poker

import org.specs2.mutable._

import axle._
import axle.game.cards._
import axle.game.poker.Implicits.pokerHandOrdering

class PokerSpec extends Specification {

  def string2cards(s: String) = s.split(",").map(Card(_))

  def string2hand(s: String): PokerHand = PokerHand(string2cards(s))

  "poker hand ranking" should {

    "work" in {

      val shared = string2cards("J♡,T♠,6♡,6♢,8♡")
      val personals = Vector("J♠,4♠","A♠,T♢","K♠,Q♢").map(string2cards(_))

      val hands = for (personal <- personals)
        yield (personal ++ shared).combinations(5).map(PokerHand(_)).max

      val jacksAndSixes = string2hand("6♡,6♢,T♠,J♠,J♡")
          
      hands.max must be equalTo jacksAndSixes
    }
  }

  "poker hand comparison" should {

    "work" in {

      string2hand("6♡,6♢,T♠,T♡,A♡") must be lessThan string2hand("6♡,6♢,T♠,J♠,J♡")
    }
  }

}