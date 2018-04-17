package axle.game.poker

import cats.Show
import cats.kernel.Order
import cats.Order.catsKernelOrderingForOrder
import cats.implicits._

import axle.game.cards.Ace
import axle.game.cards.Card

object PokerHand {

  def fromString(s: String): PokerHand =
    PokerHand(Card.fromString(s))

  implicit val showPokerHand: Show[PokerHand] = hand =>
    hand.sortedHand.reverse.map(_.show).mkString(" ")

  implicit val orderPokerHand: Order[PokerHand] = (a, b) => {
    val ac = a.category
    val bc = b.category
    val cmpCat = Order[PokerHandCategory].compare(ac, bc)
    if (cmpCat === 0) ac.compareAlike(a, b) else cmpCat
  }

}

case class PokerHand(cards: IndexedSeq[Card]) {

  lazy val sortedHand = cards.sorted.reverse

  lazy val isFlush = cards.tail.forall(_.suit === cards.head.suit)
  lazy val groups = cards.groupBy(_.rank).toList.map({ case (rank, cs) => (cs.size, rank) }).sorted.reverse

  lazy val isStraight = sortedHand.zipWithIndex.tail.forall({ case (c, i) => (sortedHand.head.rank.asInt - i) === c.rank.asInt })
  // TODO low ace

  lazy val category: PokerHandCategory =
    if (isFlush && isStraight) {
      if (sortedHand(0).rank == Ace) {
        RoyalFlush
      } else {
        StraightFlush
      }
    } else if (groups(0)._1 === 4) {
      FourOfAKind
    } else if (groups(0)._1 === 3 && groups(1)._1 === 2) {
      FullHouse
    } else if (isFlush) {
      Flush
    } else if (isStraight) {
      Straight
    } else if (groups(0)._1 === 3) {
      ThreeOfAKind
    } else if (groups(1)._1 === 2 && groups(1)._1 === 2) {
      TwoPair
    } else if (groups(0)._1 === 2) {
      Pair
    } else {
      High
    }

  def description: String = category.describe(this)

}
