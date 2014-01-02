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

  def serialize(): Char
}

object Spades extends Suit {
  override def toString() = "♠"
  def serialize() = '♠'
}

object Diamonds extends Suit {
  override def toString() = "♢"
  def serialize() = '♢'
}

object Clubs extends Suit {
  override def toString() = "♣"
  def serialize() = '♣'
}

object Hearts extends Suit {
  override def toString() = "♡"
  def serialize() = '♡'
}
