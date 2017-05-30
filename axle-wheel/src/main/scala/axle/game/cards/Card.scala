package axle.game.cards

import cats.kernel.Order
import cats.Show
import axle.string

object Card {

  def fromString(s: String): IndexedSeq[Card] =
    s.split(",").map(Card(_))

  implicit val orderCard: Order[Card] = new Order[Card] {
    def compare(a: Card, b: Card): Int = Order[Rank].compare(a.rank, b.rank)
  }

  implicit def showCard: Show[Card] = new Show[Card] {
    def show(card: Card): String =  "" + card.rank.serialize + card.suit.serialize
  }

  def apply(s: String): Card = Card(Rank(s.charAt(0)), Suit(s.charAt(1)))

}

case class Card(rank: Rank, suit: Suit) {

  def serialize: String = string(this)
}
