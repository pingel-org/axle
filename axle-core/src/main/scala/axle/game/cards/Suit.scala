package axle.game.cards

sealed trait Suit
object Spades extends Suit { override def toString() = "♠" }
object Diamonds extends Suit { override def toString() = "♢" }
object Clubs extends Suit { override def toString() = "♣" }
object Hearts extends Suit { override def toString() = "♡" }
