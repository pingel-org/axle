package axle.game.cards

import util.Random.shuffle
import cats.Show
import axle.string

case class Deck(cards: IndexedSeq[Card] = shuffle(Deck.cards))

object Deck {

  val ranks = Vector(R2, R3, R4, R5, R6, R7, R8, R9, R10, Jack, Queen, King, Ace)
  val suits = Vector(Spades, Diamonds, Clubs, Hearts)

  val cards = for {
    suit <- suits
    rank <- ranks
  } yield Card(rank, suit)

  implicit def showDeck: Show[Deck] = new Show[Deck] {
    def show(deck: Deck): String = cards.map(string(_)).mkString(" ")
  }

}
