package axle.game.cards

import spire.algebra._
import spire.implicits._

object Suit {

  implicit def suitEq: Eq[Suit] = new Eq[Suit] {
    def eqv(x: Suit, y: Suit): Boolean = x.equals(y)
  }
  
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
  override def toString: String = "♠"
  def serialize: Char = '♠'
}

object Diamonds extends Suit {
  override def toString: String = "♢"
  def serialize: Char = '♢'
}

object Clubs extends Suit {
  override def toString: String = "♣"
  def serialize: Char = '♣'
}

object Hearts extends Suit {
  override def toString: String = "♡"
  def serialize: Char = '♡'
}
