package axle.game.poker

import util.Random.shuffle
import math.Ordering
import math.Ordering.Implicits._

sealed trait Suit
object Spades extends Suit { override def toString() = "♠" }
object Diamonds extends Suit { override def toString() = "♢" }
object Clubs extends Suit { override def toString() = "♣" }
object Hearts extends Suit { override def toString() = "♡" }

sealed trait Rank {

  def asInt(): Int
}

class RankOrdering extends Ordering[Rank] {
  def compare(a: Rank, b: Rank) = a.asInt.compare(b.asInt)
}

object R2 extends Rank {
  def asInt() = 2
  override def toString() = "2"
}
object R3 extends Rank {
  def asInt() = 3
  override def toString() = "3"
}
object R4 extends Rank {
  def asInt() = 4
  override def toString() = "4"
}
object R5 extends Rank {
  def asInt() = 5
  override def toString() = "5"
}
object R6 extends Rank {
  def asInt() = 6
  override def toString() = "6"
}
object R7 extends Rank {
  def asInt() = 7
  override def toString() = "7"
}
object R8 extends Rank {
  def asInt() = 8
  override def toString() = "8"
}
object R9 extends Rank {
  def asInt() = 9
  override def toString() = "9"
}
object R10 extends Rank {
  def asInt() = 10
  override def toString() = "10"
}
object Jack extends Rank {
  def asInt() = 11
  override def toString() = "J"
}
object Queen extends Rank {
  def asInt() = 12
  override def toString() = "Q"
}
object King extends Rank {
  def asInt() = 13
  override def toString() = "K"
}
object Ace extends Rank {
  def asInt() = 14
  override def toString() = "A"
}

case class Card(rank: Rank, suit: Suit) {

  override def toString() = rank.toString + suit.toString

}

class CardOrdering extends Ordering[Card] {

  def compare(a: Card, b: Card) = {
    Implicits.rankOrdering.compare(a.rank, b.rank)
  }
}

case class Hand(cards: IndexedSeq[Card]) {

  implicit val co = Implicits.cardOrdering

  lazy val isFlush = cards.tail.forall(_.suit == cards.head.suit)
  lazy val groups = cards.groupBy(_.rank).map({ case (k, v) => (k, v.size) }).values.toList.sorted.reverse
  lazy val sortedHand = cards.sorted.reverse
  lazy val isStraight = sortedHand.zipWithIndex.tail.forall({ case (c, i) => (sortedHand.head.rank.asInt - i) == c.rank.asInt })

  def orderingStats() = (isFlush, isStraight, groups, sortedHand)

  override def toString() = sortedHand.map(_.toString).mkString(" ")
  
}

class HandOrdering extends Ordering[Hand] {

  // royal flush
  // straight flush
  // four of a kind
  // full house
  // flush
  // straight
  // three of a kind
  // two pair
  // one pair

  implicit val co = Implicits.cardOrdering

  def compare(a: Hand, b: Hand) = {
    val aos = a.orderingStats
    val bos = b.orderingStats

    if (aos == bos)
      0
    else if (aos < bos)
      -1
    else
      1
  }

}

case class Deck(cards: IndexedSeq[Card] = shuffle(Deck.cards)) {

  override def toString() = cards.map(_.toString()).mkString(" ")
}

object Deck {

  val ranks = Vector(R2, R3, R4, R5, R6, R7, R8, R9, R10, Jack, Queen, King, Ace)
  val suits = Vector(Spades, Diamonds, Clubs, Hearts)

  val cards = for {
    suit <- suits
    rank <- ranks
  } yield {
    Card(rank, suit)
  }

}

object Implicits {

  implicit val rankOrdering = new RankOrdering()
  implicit val cardOrdering = new CardOrdering()
  implicit val handOrdering = new HandOrdering()

}

