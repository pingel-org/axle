package axle.game.cards

import spire.algebra._

import cats.Show

object Suit {

  implicit def suitEq: Eq[Suit] =
    (x, y) => x.equals(y)

  implicit def show[S <: Suit]: Show[S] = _.serialize.toString

  def apply(c: Char): Suit = c match {
    case '♠' => Spades
    case '♢' => Diamonds
    case '♣' => Clubs
    case '♡' => Hearts
  }

}

sealed trait Suit {
  def serialize: Char
}

object Spades extends Suit {
  def serialize: Char = '♠'
}

object Diamonds extends Suit {
  def serialize: Char = '♢'
}

object Clubs extends Suit {
  def serialize: Char = '♣'
}

object Hearts extends Suit {
  def serialize: Char = '♡'
}
