package axle.game.cards

import spire.algebra._
import spire.implicits._

import axle.Show

object Suit {

  implicit def suitEq: Eq[Suit] = new Eq[Suit] {
    def eqv(x: Suit, y: Suit): Boolean = x.equals(y)
  }

  implicit def show[S <: Suit]: Show[S] = new Show[S] { def text(s: S) = s.serialize.toString }
  
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
