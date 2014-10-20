package axle.game.cards

import spire.implicits._
import spire.algebra._
import axle.Show
import axle.string

object Card {

  implicit def cardEq: Eq[Card] = new Eq[Card] {
    def eqv(x: Card, y: Card): Boolean = x.rank === y.rank && x.suit === y.suit
  }

  implicit object CardOrder extends Order[Card] {
    def compare(a: Card, b: Card): Int = implicitly[Order[Rank]].compare(a.rank, b.rank)
  }

  implicit def showCard: Show[Card] = new Show[Card] {
    def text(card: Card): String =  "" + card.rank.serialize + card.suit.serialize
  }
  
  def apply(s: String): Card = Card(Rank(s.charAt(0)), Suit(s.charAt(1)))

}

case class Card(rank: Rank, suit: Suit) {

  def serialize: String = string(this)
}
