package axle.game.cards

object Card {

  def apply(s: String): Card = {
    Card(Rank(s.charAt(0)), Suit(s.charAt(1)))
  }
}

case class Card(rank: Rank, suit: Suit) {

  override def toString() = rank.toString + suit.toString

  def serialize(): String = "" + rank.serialize + suit.serialize
}

class CardOrdering extends Ordering[Card] {

  def compare(a: Card, b: Card) =
    Implicits.rankOrdering.compare(a.rank, b.rank)

}
