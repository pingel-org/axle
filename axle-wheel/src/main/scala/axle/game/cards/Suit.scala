package axle.game.cards

import cats.Show
import cats.kernel.Order
import cats.implicits._

object Suit {

  implicit val orderSuit: Order[Suit] =
    (a, b) => Order[Char].compare(a.serialize, b.serialize)

  implicit def show[S <: Suit]: Show[S] = s => axle.showChar.show(s.serialize)

  def apply(c: Char): Suit = c match {
    case '♠' => Spades
    case '♢' => Diamonds
    case '♣' => Clubs
    case '♡' => Hearts
    case 'S' => Spades
    case 'D' => Diamonds
    case 'C' => Clubs
    case 'H' => Hearts
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
