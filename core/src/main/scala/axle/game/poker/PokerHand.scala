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
      StraightFlush(sortedHand(0))
    else if (groups(0)._1 == 4)
      FourOfAKind(groups(0)._2)
    else if (groups(0)._1 == 3 && groups(1)._1 == 2)
      FullHouse(groups(0)._2, groups(1)._2)
    else if (isFlush)
      Flush(sortedHand(0).suit)
    else if (isStraight)
      Straight(sortedHand(0).rank)
    else if (groups(0)._1 == 3)
      ThreeOfAKind(groups(0)._2)
    else if (groups(1)._1 == 2 && groups(1)._1 == 2)
      TwoPair(groups(0)._2, groups(1)._2)
    else if (groups(0)._1 == 2)
      Pair(groups(0)._2)
    else
      High(sortedHand(0).rank)

  override def toString() = sortedHand.map(_.toString).mkString(" ")

}

class PokerHandOrdering extends Ordering[PokerHand] {

  import math.Ordering
  import math.Ordering.Implicits._

  def compare(a: PokerHand, b: PokerHand) = {
    val ac = a.category
    val bc = b.category

    val cmpCat = pokerHandCategoryOrdering.compare(ac, bc)
    if (cmpCat == 0)
      if (a.sortedHand < b.sortedHand)
        -1
      else if (a.sortedHand > b.sortedHand)
        1
      else
        0
    else
      cmpCat

  }

}
