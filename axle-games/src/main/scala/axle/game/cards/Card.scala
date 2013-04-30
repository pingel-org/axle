package axle.game.cards

case class Card(rank: Rank, suit: Suit) {

  override def toString() = rank.toString + suit.toString

}

class CardOrdering extends Ordering[Card] {

  def compare(a: Card, b: Card) = {
    Implicits.rankOrdering.compare(a.rank, b.rank)
  }
}
