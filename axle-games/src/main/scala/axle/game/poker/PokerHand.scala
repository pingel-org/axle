package axle.game.poker

import axle.game.cards._
import axle.game.cards.Implicits._
import axle.game.poker.Implicits._

case class PokerHand(cards: IndexedSeq[Card]) {

  lazy val sortedHand = cards.sorted.reverse

  lazy val isFlush = cards.tail.forall(_.suit == cards.head.suit)
  lazy val groups = cards.groupBy(_.rank).toList.map({ case (rank, cs) => (cs.size, rank) }).sorted.reverse

  lazy val isStraight = sortedHand.zipWithIndex.tail.forall({ case (c, i) => (sortedHand.head.rank.asInt - i) == c.rank.asInt })
  // TODO low ace

  lazy val category: PokerHandCategory =
    if (isFlush && isStraight)
      if (sortedHand(0).rank == Ace)
        RoyalFlush
      else
        StraightFlush
    else if (groups(0)._1 == 4)
      FourOfAKind
    else if (groups(0)._1 == 3 && groups(1)._1 == 2)
      FullHouse
    else if (isFlush)
      Flush
    else if (isStraight)
      Straight
    else if (groups(0)._1 == 3)
      ThreeOfAKind
    else if (groups(1)._1 == 2 && groups(1)._1 == 2)
      TwoPair
    else if (groups(0)._1 == 2)
      Pair
    else
      High

  override def toString() = sortedHand.reverse.map(_.toString).mkString(" ")

  override def equals(other: Any): Boolean = other match {
    case otherHand : PokerHand => sortedHand equals otherHand.sortedHand
    case _ => false
  }

  def description() = category.describe(this)
  
}

class PokerHandOrdering extends Ordering[PokerHand] {

  import math.Ordering
  import math.Ordering.Implicits._

  def compare(a: PokerHand, b: PokerHand) = {
    val ac = a.category
    val bc = b.category

    val cmpCat = pokerHandCategoryOrdering.compare(ac, bc)
    if (cmpCat == 0)
      ac.compareAlike(a, b)
    else
      cmpCat

  }

}
