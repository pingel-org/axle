package axle.game.poker

import axle.game.cards._

sealed trait PokerHandCategory {
  def asInt(): Int
  def name(): String
}

case class RoyalFlush(suit: Suit) extends PokerHandCategory {
  def asInt() = 9
  def name() = "royal flush"
}

case class StraightFlush(to: Card) extends PokerHandCategory {
  def asInt() = 8
  def name() = "straight flush"
}

case class FourOfAKind(rank: Rank) extends PokerHandCategory {
  def asInt() = 7
  def name() = "four of a kind"
}

case class FullHouse(three: Rank, two: Rank) extends PokerHandCategory {
  def asInt() = 6
  def name() = "full house"
}

case class Flush(suit: Suit) extends PokerHandCategory {
  def asInt() = 5
  def name() = "flush"
}

case class Straight(to: Rank) extends PokerHandCategory {
  def asInt() = 4
  def name() = "straight"
}

case class ThreeOfAKind(rank: Rank) extends PokerHandCategory {
  def asInt() = 3
  def name() = "three of a kind"
}

case class TwoPair(high: Rank, low: Rank) extends PokerHandCategory {
  def asInt() = 2
  def name() = "two pair"
}

case class Pair(rank: Rank) extends PokerHandCategory {
  def asInt() = 1
  def name() = "pair"
}

case class High(rank: Rank) extends PokerHandCategory {
  def asInt() = 0
  def name() = "high"
}

class PokerHandCategoryOrdering extends Ordering[PokerHandCategory] {

  def compare(a: PokerHandCategory, b: PokerHandCategory): Int =
    a.asInt.compare(b.asInt)

}