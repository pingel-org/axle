package axle.game.cards

import cats.kernel.Order
import cats.Show

object Rank {

  implicit val orderRank: Order[Rank] = (a, b) => a.asInt.compare(b.asInt)

  implicit def show[R <: Rank]: Show[R] = _.serialize.toString

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

trait R2 extends Rank {
  def asInt: Int = 2
  def serialize: Char = '2'
}
object R2 extends R2

trait R3 extends Rank {
  def asInt: Int = 3
  def serialize: Char = '3'
}
object R3 extends R3

trait R4 extends Rank {
  def asInt: Int = 4
  def serialize: Char = '4'
}
object R4 extends R4

trait R5 extends Rank {
  def asInt: Int = 5
  def serialize: Char = '5'
}
object R5 extends R5

trait R6 extends Rank {
  def asInt: Int = 6
  def serialize: Char = '6'
}
object R6 extends R6

trait R7 extends Rank {
  def asInt: Int = 7
  def serialize: Char = '7'
}
object R7 extends R7

trait R8 extends Rank {
  def asInt: Int = 8
  def serialize: Char = '8'
}
object R8 extends R8

trait R9 extends Rank {
  def asInt: Int = 9
  def serialize: Char = '9'
}
object R9 extends R9

trait R10 extends Rank {
  def asInt: Int = 10
  def serialize: Char = 'T'
}
object R10 extends R10

trait Jack extends Rank {
  def asInt: Int = 11
  def serialize: Char = 'J'
}
object Jack extends Jack

trait Queen extends Rank {
  def asInt: Int = 12
  def serialize: Char = 'Q'
}
object Queen extends Queen

trait King extends Rank {
  def asInt: Int = 13
  def serialize: Char = 'K'
}
object King extends King

trait Ace extends Rank {
  def asInt: Int = 14
  def serialize: Char = 'A'
}
object Ace extends Ace
