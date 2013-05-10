package axle.game.cards

object Rank {
  
  def apply(c: Char) = c match {
    case '2' => R2
    case '3' => R3
    case '4' => R4
    case '5' => R5
    case '6' => R6
    case '7' => R7
    case '8' => R8
    case '9' => R9
    case 'T' => R10
    case 'J' => Jack
    case 'Q' => Queen
    case 'K' => King
    case 'A' => Ace
  }
}

sealed trait Rank {

  def asInt(): Int

  def serialize(): Char

}

class RankOrdering extends Ordering[Rank] {
  def compare(a: Rank, b: Rank) = a.asInt.compare(b.asInt)
}

object R2 extends Rank {
  def asInt() = 2
  override def toString() = "2"
  def serialize() = '2'
}

object R3 extends Rank {
  def asInt() = 3
  override def toString() = "3"
  def serialize() = '3'
}

object R4 extends Rank {
  def asInt() = 4
  override def toString() = "4"
  def serialize() = '4'
}

object R5 extends Rank {
  def asInt() = 5
  override def toString() = "5"
  def serialize() = '5'
}

object R6 extends Rank {
  def asInt() = 6
  override def toString() = "6"
  def serialize() = '6'
}

object R7 extends Rank {
  def asInt() = 7
  override def toString() = "7"
  def serialize() = '7'
}

object R8 extends Rank {
  def asInt() = 8
  override def toString() = "8"
  def serialize() = '8'
}

object R9 extends Rank {
  def asInt() = 9
  override def toString() = "9"
  def serialize() = '9'
}

object R10 extends Rank {
  def asInt() = 10
  override def toString() = "10"
  def serialize() = 'T'
}

object Jack extends Rank {
  def asInt() = 11
  override def toString() = "J"
  def serialize() = 'J'
}

object Queen extends Rank {
  def asInt() = 12
  override def toString() = "Q"
  def serialize() = 'Q'
}

object King extends Rank {
  def asInt() = 13
  override def toString() = "K"
  def serialize() = 'K'
}

object Ace extends Rank {
  def asInt() = 14
  override def toString() = "A"
  def serialize() = 'A'
}
