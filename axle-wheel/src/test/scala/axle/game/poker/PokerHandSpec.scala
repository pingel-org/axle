package axle.game.poker

import org.scalatest.funsuite._
import org.scalatest.matchers.should.Matchers
import cats.kernel.Eq
import cats.Order.catsKernelOrderingForOrder
import cats.implicits._

class PokerHandSpec extends AnyFunSuite with Matchers {

  test("poker hand ranking") {

    val shared = PokerHand.fromString("J♡,T♠,6♡,6♢,8♡")
    val personals = Vector("J♠,4♠", "A♠,T♢", "K♠,Q♢").map(PokerHand.fromString)

    val hands = personals map { personal =>
      (personal.cards ++ shared.cards).combinations(5).map(PokerHand.apply).max
    }

    val jacksAndSixes = PokerHand.fromString("6♡,6♢,T♠,J♠,J♡")

    Eq[PokerHand].eqv(hands.max, jacksAndSixes) should be(true)
  }

  test("ace high < pair of 6s") {
    val hand = PokerHand.fromString("3♡,6♢,9♠,T♡,A♡")
    hand.description should include("high")
    hand should be < PokerHand.fromString("6♡,6♢,T♠,J♠,4♡")
  }

  test("straight > two pair") {
    val hand = PokerHand.fromString("7♡,6♠,8♡,5♠,9♢")
    hand.description should include("straight")
    hand.show should be("5♠ 6♠ 7♡ 8♡ 9♢")
    hand should be > PokerHand.fromString("6♡,6♢,T♠,T♠,4♡")
  }

  test("flush > two pair") {
    val hand = PokerHand.fromString("6♡,3♡,9♡,T♡,A♡")
    hand.description should include("flush")
    hand.show should be("3♡ 6♡ 9♡ T♡ A♡")
    hand should be > PokerHand.fromString("6♡,6♢,T♠,T♠,4♡")
  }

  test("straight flush > two pair") {
    val hand = PokerHand.fromString("2♡,3♡,4♡,5♡,6♡")
    hand.description should include("straight flush")
    hand should be > PokerHand.fromString("6♡,6♢,T♠,T♠,4♡")
  }

  test("royal flush > two pair") {
    val hand = PokerHand.fromString("T♡,J♡,Q♡,K♡,A♡")
    hand.description should include("royal")
    hand should be > PokerHand.fromString("6♡,6♢,T♠,T♠,4♡")
  }

  test("two pair vs two pair") {
    val hand = PokerHand.fromString("6♡,6♢,T♠,T♡,A♡")
    hand.description should include("two pair")
    hand should be < PokerHand.fromString("6♡,6♢,T♠,J♠,J♡")
  }

  test("full house vs two pair") {
    val hand = PokerHand.fromString("6♡,6♢,T♠,T♡,T♢")
    hand.description should include("full house")
    hand should be > PokerHand.fromString("6♡,6♢,T♠,J♠,J♡")
  }

  test("pair vs pair") {
    val hand = PokerHand.fromString("6♡,6♢,8♠,9♡,K♡")
    hand.description should include("pair")
    hand should be < PokerHand.fromString("K♡,K♢,2♠,3♠,5♡")
  }

  test("three-of-a-kind vs three-of-a-kind") {
    val hand = PokerHand.fromString("6♡,6♢,6♠,Q♡,K♡")
    hand.description should include("three")
    hand should be < PokerHand.fromString("7♡,7♢,7♠,3♠,4♡")
  }

  test("four-of-a-kind vs four-of-a-kind") {
    val hand = PokerHand.fromString("6♡,6♢,6♠,6♣,Q♡")
    hand.description should include("four")
    hand should be < PokerHand.fromString("7♡,7♢,7♠,7♣,2♡")
  }

}
