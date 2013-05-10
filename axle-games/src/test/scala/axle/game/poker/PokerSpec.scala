package axle.game.poker

import org.specs2.mutable._

import axle._
import axle.game.cards._
import axle.game.poker.Implicits.pokerHandOrdering

class PokerSpec extends Specification {

  "poker hand ranking" should {

    "work" in {

      val shared = "J♡,T♠,6♡,6♢,8♡".split(",").map(Card(_))
      val smallHands = "J♠,4♠-A♠,T♢-K♠,Q♢".split("-").map(_.split(",").map(Card(_)))

      val hands =
        for (smallHand <- smallHands)
          yield (smallHand ++ shared).combinations(5).map(PokerHand(_)).max

      val jacksAndSixes = PokerHand("6♡,6♢,T♠,J♠,J♡".split(",").map(Card(_)))
          
      hands.max must be equalTo jacksAndSixes
    }
  }

}