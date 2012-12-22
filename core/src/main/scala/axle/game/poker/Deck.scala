package axle.game.poker

import util.Random.shuffle

sealed trait Suit
object Spades extends Suit { override def toString() = "♠" }
object Diamonds extends Suit { override def toString() = "♢" }
object Clubs extends Suit { override def toString() = "♣" }
object Hearts extends Suit { override def toString() = "♡" }

sealed trait Rank
object R2 extends Rank { override def toString() = "2" }
object R3 extends Rank { override def toString() = "3" }
object R4 extends Rank { override def toString() = "4" }
object R5 extends Rank { override def toString() = "5" }
object R6 extends Rank { override def toString() = "6" }
object R7 extends Rank { override def toString() = "7" }
object R8 extends Rank { override def toString() = "8" }
object R9 extends Rank { override def toString() = "9" }
object R10 extends Rank { override def toString() = "10" }
object Jack extends Rank { override def toString() = "J" }
object Queen extends Rank { override def toString() = "Q" }
object King extends Rank { override def toString() = "K" }
object Ace extends Rank { override def toString() = "A" }

case class Card(rank: Rank, suit: Suit) {
  override def toString() = rank.toString + suit.toString
}

case class Deck(cards: IndexedSeq[Card] = shuffle(Deck.cards)) {
  
  override def toString() = cards.map(_.toString()).mkString(" ")
}

object Deck {

  val ranks = Vector(R2, R3, R4, R5, R6, R7, R8, R9, R10, Jack, Queen, King, Ace)

  val suits = Vector(Spades, Diamonds, Clubs, Hearts)

  val cards = for {
    suit <- suits
    rank <- ranks
  } yield {
    Card(rank, suit)
  }

}
