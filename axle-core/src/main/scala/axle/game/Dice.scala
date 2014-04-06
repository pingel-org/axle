package axle.game

import axle.stats._
import spire.math._
import spire.algebra._
import spire.random._
import spire.implicits._

object Dice {

  def die(n: Int): RandomVariable[Int, Rational] =
    RandomVariable0[Int, Rational](
      "d" + n,
      Some(1 to n),
      distribution = Some(new ConditionalProbabilityTable0((1 to n).map(i => (i, Rational(1, n))).toMap)))

  val sixth = Rational(1, 6)

  def utfD6: RandomVariable[Symbol, Rational] = RandomVariable0("UTF d6",
    Some(List('⚀, '⚁, '⚂, '⚃, '⚄, '⚅).toIndexedSeq),
    distribution = Some(new ConditionalProbabilityTable0(Map(
      '⚀ -> sixth,
      '⚁ -> sixth,
      '⚂ -> sixth,
      '⚃ -> sixth,
      '⚄ -> sixth,
      '⚅ -> sixth))))

}
