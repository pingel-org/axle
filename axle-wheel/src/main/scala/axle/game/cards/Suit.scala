package axle.game.cards

import cats.Show
import cats.kernel.Eq

object Suit {

  implicit def suitEq = Eq.fromUniversalEquals[Suit]

  implicit def show[S <: Suit]: Show[S] = s => axle.showChar.show(s.serialize)

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
