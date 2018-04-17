package axle.game.poker

import cats.kernel.Order
import cats.Show
import cats.Order.catsKernelOrderingForOrder
import cats.implicits._
import axle.game.cards._

object PokerHandCategory {

  implicit val orderPokerHandCategory: Order[PokerHandCategory] = (a, b) => a.asInt.compare(b.asInt)

  val categories = Vector(
    High,
    Pair,
    TwoPair,
    ThreeOfAKind,
    Straight,
    Flush,
    FullHouse,
    FourOfAKind,
    StraightFlush,
    RoyalFlush).sorted

  implicit val showPHC: Show[PokerHandCategory] = _.name

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
  def specifics(hand: PokerHand): String = "in " + suit(hand).show
  def suit(hand: PokerHand): Suit = hand.sortedHand(0).suit
}

object StraightFlush extends PokerHandCategory {
  def asInt: Int = 8
  def name: String = "straight flush"
  def specifics(hand: PokerHand): String = "to " + to(hand).show
  def to(hand: PokerHand): Card = hand.sortedHand(0)
}

object FourOfAKind extends PokerHandCategory {
  def asInt: Int = 7
  def name: String = "four of a kind"
  def specifics(hand: PokerHand): String = "of " + rank(hand).show
  def rank(hand: PokerHand): Rank = hand.groups(0)._2
}

object FullHouse extends PokerHandCategory {

  def asInt: Int = 6
  def name: String = "full house"
  def specifics(hand: PokerHand): String = three(hand).show + " over " + two(hand).show
  def three(hand: PokerHand): Rank = hand.groups(0)._2
  def two(hand: PokerHand): Rank = hand.groups(1)._2

}

object Flush extends PokerHandCategory {

  def asInt: Int = 5
  def name: String = "flush"
  def specifics(hand: PokerHand): String = "in " + suit(hand).show
  def suit(hand: PokerHand): Suit = hand.sortedHand(0).suit

}

object Straight extends PokerHandCategory {

  def asInt: Int = 4
  def name: String = "straight"
  def specifics(hand: PokerHand): String = "to " + to(hand).show
  def to(hand: PokerHand): Rank = hand.sortedHand(0).rank

}

object ThreeOfAKind extends PokerHandCategory {

  def asInt: Int = 3
  def name: String = "three of a kind"
  def specifics(hand: PokerHand): String = "of " + rank(hand).show
  def rank(hand: PokerHand): Rank = hand.groups(0)._2

}

object TwoPair extends PokerHandCategory {

  def asInt: Int = 2
  def name: String = "two pair"
  def specifics(hand: PokerHand): String = high(hand).show + " and " + low(hand).show
  def high(hand: PokerHand): Rank = hand.groups(0)._2
  def low(hand: PokerHand): Rank = hand.groups(1)._2

}

object Pair extends PokerHandCategory {

  def asInt: Int = 1
  def name: String = "pair"
  def specifics(hand: PokerHand): String = "of " + rank(hand).show
  def rank(hand: PokerHand): Rank = hand.groups(0)._2

}

object High extends PokerHandCategory {

  def asInt: Int = 0
  def name: String = "high"
  def specifics(hand: PokerHand): String = "" + rank(hand).show + " " + name
  def rank(hand: PokerHand): Rank = hand.sortedHand(0).rank

}
