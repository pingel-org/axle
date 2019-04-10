package axle.game.cards

import cats.Show
import cats.implicits._
import spire.random.Generator
import spire.random.Generator.rng

case class Deck(cards: List[Card] = axle.shuffle(Deck.cards)(rng))

object Deck {

  val ranks = Vector(R2, R3, R4, R5, R6, R7, R8, R9, R10, Jack, Queen, King, Ace)
  val suits = Vector(Spades, Diamonds, Clubs, Hearts)

  val cards: List[Card] = (for {
    suit <- suits
    rank <- ranks
  } yield Card(rank, suit)).toList

  implicit def showDeck: Show[Deck] = deck =>
    deck.cards.map(_.show).mkString(" ")

  def riffle(left: List[Card], right: List[Card], booleanStream: Stream[Boolean]): List[Card] =
    if( left.isEmpty ) {
      right
    } else if ( right.isEmpty ) {
      left
    } else if ( booleanStream.head ) {
      left.head :: riffle(left.tail, right, booleanStream.tail)
    } else {
      right.head :: riffle(left, right.tail, booleanStream.tail)
    }

  def riffleShuffle(deck: Deck, rng: Generator): Deck = {

    val splitAt = rng.nextInt(1, deck.cards.size -1)
    val booleanStream = Stream.continually(spire.random.Generator.rng.nextBoolean)

    val left = deck.cards.take(splitAt)
    val right = deck.cards.drop(splitAt)

    Deck(riffle(left, right, booleanStream))
  }
}
