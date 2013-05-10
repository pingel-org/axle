package axle.game.poker

import axle.game.cards._
import axle.game.cards.Implicits.{ rankOrdering, cardOrdering }
import math.Ordering
import math.Ordering.Implicits._

sealed trait PokerHandCategory {

  def asInt(): Int
  def name(): String
  def describe(hand: PokerHand) = name() + " " + specifics(hand)
  def specifics(hand: PokerHand): String
  def compareAlike(a: PokerHand, b: PokerHand): Int =
    if (a.sortedHand < b.sortedHand)
      -1
    else if (a.sortedHand > b.sortedHand)
      1
    else
      0
}

object RoyalFlush extends PokerHandCategory {
  def asInt() = 9
  def name() = "royal flush"
  def specifics(hand: PokerHand) = "in " + suit(hand)
  def suit(hand: PokerHand) = hand.sortedHand(0).suit
}

object StraightFlush extends PokerHandCategory {
  def asInt() = 8
  def name() = "straight flush"
  def specifics(hand: PokerHand) = "to " + to(hand)
  def to(hand: PokerHand) = hand.sortedHand(0)
}

object FourOfAKind extends PokerHandCategory {
  def asInt() = 7
  def name() = "four of a kind"
  def specifics(hand: PokerHand) = "of " + rank(hand)
  def rank(hand: PokerHand) = hand.groups(0)._2
}

object FullHouse extends PokerHandCategory {

  def asInt() = 6
  def name() = "full house"
  def specifics(hand: PokerHand) = three(hand) + " over " + two(hand)
  def three(hand: PokerHand) = hand.groups(0)._2
  def two(hand: PokerHand) = hand.groups(1)._2
  override def compareAlike(a: PokerHand, b: PokerHand): Int = {
    val cmp3 = rankOrdering.compare(three(a), three(b))
    if (cmp3 == 0)
      rankOrdering.compare(two(a), two(b))
    else
      cmp3
  }
}

object Flush extends PokerHandCategory {
  def asInt() = 5
  def name() = "flush"
  def specifics(hand: PokerHand) = "in " + suit(hand)
  def suit(hand: PokerHand) = hand.sortedHand(0).suit
}

object Straight extends PokerHandCategory {
  def asInt() = 4
  def name() = "straight"
  def specifics(hand: PokerHand) = "to " + to(hand)
  def to(hand: PokerHand) = hand.sortedHand(0).rank
}

object ThreeOfAKind extends PokerHandCategory {
  def asInt() = 3
  def name() = "three of a kind"
  def specifics(hand: PokerHand) = "of " + rank(hand)
  def rank(hand: PokerHand) = hand.groups(0)._2
}

object TwoPair extends PokerHandCategory {
  def asInt() = 2
  def name() = "two pair"
  def specifics(hand: PokerHand) = high(hand) + " and " + low(hand)
  def high(hand: PokerHand) = hand.groups(0)._2
  def low(hand: PokerHand) = hand.groups(1)._2
  def remaining(hand: PokerHand) = hand.groups(2)._2

  override def compareAlike(a: PokerHand, b: PokerHand): Int = {
    val cmpH = rankOrdering.compare(high(a), high(b))
    if (cmpH == 0) {
      val cmpL = rankOrdering.compare(low(a), low(b))
      if( cmpL == 0 ) {
        rankOrdering.compare(remaining(a), remaining(b))
      } else cmpL
    } else cmpH
  }

}

object Pair extends PokerHandCategory {
  def asInt() = 1
  def name() = "pair"
  def specifics(hand: PokerHand) = "of " + rank(hand)
  def rank(hand: PokerHand) = hand.groups(0)._2
}

object High extends PokerHandCategory {
  def asInt() = 0
  def name() = "high"
  override def describe(hand: PokerHand) = rank(hand) + " " + name()
  def specifics(hand: PokerHand) = "" + rank(hand)
  def rank(hand: PokerHand) = hand.sortedHand(0).rank
}

class PokerHandCategoryOrdering extends Ordering[PokerHandCategory] {

  def compare(a: PokerHandCategory, b: PokerHandCategory): Int =
    a.asInt.compare(b.asInt)

}