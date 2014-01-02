package axle.game.cards

object Suit {

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
