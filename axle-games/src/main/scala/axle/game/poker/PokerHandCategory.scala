package axle.game.poker

import axle.game.cards._
import spire.algebra.Order
import spire.compat.ordering
import axle.string

object PokerHandCategory {

  implicit object PokerHandCategoryOrder extends Order[PokerHandCategory] {
    def compare(a: PokerHandCategory, b: PokerHandCategory): Int =
      a.asInt.compare(b.asInt)
  }

  val categories = Vector(High, Pair, TwoPair, ThreeOfAKind, Straight, Flush, FullHouse, FourOfAKind, StraightFlush, RoyalFlush).sorted

}

sealed trait PokerHandCategory {

  def asInt: Int
  def name: String
  def describe(hand: PokerHand): String = name + " " + specifics(hand)
  def specifics(hand: PokerHand): String

  def compareAlike(a: PokerHand, b: PokerHand): Int =
    a.groups.map(_._2).zip(b.groups.map(_._2))
      .map({ case (ar, br) => Order[Rank].compare(ar, br) })
      .find(_ != 0)
      .getOrElse(0)

}

object RoyalFlush extends PokerHandCategory {
  def asInt: Int = 9
  def name: String = "royal flush"
  def specifics(hand: PokerHand): String = "in " + string(suit(hand))
  def suit(hand: PokerHand): Suit = hand.sortedHand(0).suit
}

object StraightFlush extends PokerHandCategory {
  def asInt: Int = 8
  def name: String = "straight flush"
  def specifics(hand: PokerHand): String = "to " + string(to(hand))
  def to(hand: PokerHand): Card = hand.sortedHand(0)
}

object FourOfAKind extends PokerHandCategory {
  def asInt: Int = 7
  def name: String = "four of a kind"
  def specifics(hand: PokerHand): String = "of " + string(rank(hand))
  def rank(hand: PokerHand): Rank = hand.groups(0)._2
}

object FullHouse extends PokerHandCategory {

  def asInt: Int = 6
  def name: String = "full house"
  def specifics(hand: PokerHand): String = string(three(hand)) + " over " + string(two(hand))
  def three(hand: PokerHand): Rank = hand.groups(0)._2
  def two(hand: PokerHand): Rank = hand.groups(1)._2

}

object Flush extends PokerHandCategory {

  def asInt: Int = 5
  def name: String = "flush"
  def specifics(hand: PokerHand): String = "in " + string(suit(hand))
  def suit(hand: PokerHand): Suit = hand.sortedHand(0).suit

}

object Straight extends PokerHandCategory {

  def asInt: Int = 4
  def name: String = "straight"
  def specifics(hand: PokerHand): String = "to " + string(to(hand))
  def to(hand: PokerHand): Rank = hand.sortedHand(0).rank

}

object ThreeOfAKind extends PokerHandCategory {

  def asInt: Int = 3
  def name: String = "three of a kind"
  def specifics(hand: PokerHand): String = "of " + string(rank(hand))
  def rank(hand: PokerHand): Rank = hand.groups(0)._2

}

object TwoPair extends PokerHandCategory {

  def asInt: Int = 2
  def name: String = "two pair"
  def specifics(hand: PokerHand): String = string(high(hand)) + " and " + string(low(hand))
  def high(hand: PokerHand): Rank = hand.groups(0)._2
  def low(hand: PokerHand): Rank = hand.groups(1)._2

}

object Pair extends PokerHandCategory {

  def asInt: Int = 1
  def name: String = "pair"
  def specifics(hand: PokerHand): String = "of " + string(rank(hand))
  def rank(hand: PokerHand): Rank = hand.groups(0)._2

}

object High extends PokerHandCategory {

  def asInt: Int = 0
  def name: String = "high"
  override def describe(hand: PokerHand): String = string(rank(hand)) + " " + name
  def specifics(hand: PokerHand): String = "" + string(rank(hand))
  def rank(hand: PokerHand): Rank = hand.sortedHand(0).rank

}
