package axle.game.cards

import spire.implicits._
import spire.algebra._

object Card {

  implicit def cardEq: Eq[Card] = new Eq[Card] {
    def eqv(x: Card, y: Card): Boolean = x.rank === y.rank && x.suit === y.suit
  }

  implicit object CardOrder extends Order[Card] {
    def compare(a: Card, b: Card): Int = implicitly[Order[Rank]].compare(a.rank, b.rank)
  }

  def apply(s: String): Card = Card(Rank(s.charAt(0)), Suit(s.charAt(1)))

}

case class Card(rank: Rank, suit: Suit) {

  override def toString: String = rank.toString + suit.toString

  def serialize: String = "" + rank.serialize + suit.serialize
}
