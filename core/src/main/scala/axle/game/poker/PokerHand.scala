package axle.game.cards

case class PokerHand(cards: IndexedSeq[Card]) {

  implicit val co = Implicits.cardOrdering

  lazy val isFlush = cards.tail.forall(_.suit == cards.head.suit)
  lazy val groups = cards.groupBy(_.rank).map({ case (k, v) => (k, v.size) }).values.toList.sorted.reverse
  lazy val sortedHand = cards.sorted.reverse
  lazy val isStraight = sortedHand.zipWithIndex.tail.forall({ case (c, i) => (sortedHand.head.rank.asInt - i) == c.rank.asInt })

  // TODO low ace
  
  def orderingStats() = (
    isFlush && isStraight,
    groups(0) == 4,
    groups(0) == 3 && groups(1) == 2,
    isFlush,
    isStraight,
    groups,
    sortedHand)

  override def toString() = sortedHand.map(_.toString).mkString(" ")

}

class PokerHandOrdering extends Ordering[PokerHand] {

  import math.Ordering
  import math.Ordering.Implicits._

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

  def compare(a: PokerHand, b: PokerHand) = {
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
