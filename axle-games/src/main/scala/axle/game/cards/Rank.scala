package axle.game.cards

object Rank {

  def apply(c: Char): Rank = c match {
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

  def asInt: Int

  def serialize: Char

}

class RankOrdering extends Ordering[Rank] {
  def compare(a: Rank, b: Rank): Int = a.asInt.compare(b.asInt)
}

object R2 extends Rank {
  def asInt: Int = 2
  override def toString: String = "2"
  def serialize: Char = '2'
}

object R3 extends Rank {
  def asInt: Int = 3
  override def toString: String = "3"
  def serialize: Char = '3'
}

object R4 extends Rank {
  def asInt: Int = 4
  override def toString: String = "4"
  def serialize: Char = '4'
}

object R5 extends Rank {
  def asInt: Int = 5
  override def toString: String = "5"
  def serialize: Char = '5'
}

object R6 extends Rank {
  def asInt: Int = 6
  override def toString: String = "6"
  def serialize: Char = '6'
}

object R7 extends Rank {
  def asInt: Int = 7
  override def toString: String = "7"
  def serialize: Char = '7'
}

object R8 extends Rank {
  def asInt: Int = 8
  override def toString: String = "8"
  def serialize: Char = '8'
}

object R9 extends Rank {
  def asInt: Int = 9
  override def toString: String = "9"
  def serialize: Char = '9'
}

object R10 extends Rank {
  def asInt: Int = 10
  override def toString: String = "10"
  def serialize: Char = 'T'
}

object Jack extends Rank {
  def asInt: Int = 11
  override def toString: String = "J"
  def serialize: Char = 'J'
}

object Queen extends Rank {
  def asInt: Int = 12
  override def toString: String = "Q"
  def serialize: Char = 'Q'
}

object King extends Rank {
  def asInt: Int = 13
  override def toString: String = "K"
  def serialize: Char = 'K'
}

object Ace extends Rank {
  def asInt: Int = 14
  override def toString: String = "A"
  def serialize: Char = 'A'
}
