package axle.game.poker

import cats.Show
import axle.game.cards.Ace
import axle.game.cards.Card
import axle.string
import spire.algebra.Order
import spire.compat.ordering
import spire.implicits.IntAlgebra
import spire.implicits.SeqOrder
import spire.implicits.eqOps

object PokerHand {

  def fromString(s: String): PokerHand =
    PokerHand(Card.fromString(s))

  implicit val showPokerHand: Show[PokerHand] = new Show[PokerHand] {
    def show(hand: PokerHand): String = hand.sortedHand.reverse.map(string(_)).mkString(" ")
  }

  implicit val orderPokerHand: Order[PokerHand] = new Order[PokerHand] {

    def compare(a: PokerHand, b: PokerHand): Int = {
      val ac = a.category
      val bc = b.category
      val cmpCat = Order[PokerHandCategory].compare(ac, bc)
      if (cmpCat === 0) ac.compareAlike(a, b) else cmpCat
    }

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
