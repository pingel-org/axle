package axle.game.cards

sealed trait Rank {

  def asInt(): Int
}

class RankOrdering extends Ordering[Rank] {
  def compare(a: Rank, b: Rank) = a.asInt.compare(b.asInt)
}

object R2 extends Rank {
  def asInt() = 2
  override def toString() = "2"
}
object R3 extends Rank {
  def asInt() = 3
  override def toString() = "3"
}
object R4 extends Rank {
  def asInt() = 4
  override def toString() = "4"
}
object R5 extends Rank {
  def asInt() = 5
  override def toString() = "5"
}
object R6 extends Rank {
  def asInt() = 6
  override def toString() = "6"
}
object R7 extends Rank {
  def asInt() = 7
  override def toString() = "7"
}
object R8 extends Rank {
  def asInt() = 8
  override def toString() = "8"
}
object R9 extends Rank {
  def asInt() = 9
  override def toString() = "9"
}
object R10 extends Rank {
  def asInt() = 10
  override def toString() = "10"
}
object Jack extends Rank {
  def asInt() = 11
  override def toString() = "J"
}
object Queen extends Rank {
  def asInt() = 12
  override def toString() = "Q"
}
object King extends Rank {
  def asInt() = 13
  override def toString() = "K"
}
object Ace extends Rank {
  def asInt() = 14
  override def toString() = "A"
}
