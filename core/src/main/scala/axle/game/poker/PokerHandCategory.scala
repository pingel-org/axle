package axle.game.poker

import axle.game.cards._

sealed trait PokerHandCategory {
  def asInt(): Int
}

case class RoyalFlush(suit: Suit) extends PokerHandCategory {
  def asInt() = 9
}

case class StraightFlush(to: Card) extends PokerHandCategory {
  def asInt() = 8
}

case class FourOfAKind(rank: Rank) extends PokerHandCategory {
  def asInt() = 7
}

case class FullHouse(three: Rank, two: Rank) extends PokerHandCategory {
  def asInt() = 6
}

case class Flush(suit: Suit) extends PokerHandCategory {
  def asInt() = 5
}

case class Straight(to: Rank) extends PokerHandCategory {
  def asInt() = 4
}

case class ThreeOfAKind(rank: Rank) extends PokerHandCategory {
  def asInt() = 3
}

case class TwoPair(high: Rank, low: Rank) extends PokerHandCategory {
  def asInt() = 2
}

case class Pair(rank: Rank) extends PokerHandCategory {
  def asInt() = 1
}

case class High(rank: Rank) extends PokerHandCategory {
  def asInt() = 0
}

class PokerHandCategoryOrdering extends Ordering[PokerHandCategory] {

  def compare(a: PokerHandCategory, b: PokerHandCategory): Int =
    a.asInt.compare(b.asInt)

}