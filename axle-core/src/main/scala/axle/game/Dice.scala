package axle.game

import axle.stats._
import collection._
import spire.math._
import spire.implicits._

object Dice {
  
  def die(n: Int) = RandomVariable0("d"+n, Some(1 to n),
    distribution = Some(new ConditionalProbabilityTable0((1 to n).map(i => (i, Real(Rational(1, n)))).toMap))) // TODO: avoid wrapping Rational in Real

  val sixth = Rational(1, 6)
  
  def utfD6() = RandomVariable0("UTF d6",
    Some(List('⚀, '⚁, '⚂, '⚃, '⚄, '⚅).toIndexedSeq),
    distribution = Some(new ConditionalProbabilityTable0(immutable.Map(
      '⚀ -> sixth,
      '⚁ -> sixth,
      '⚂ -> sixth,
      '⚃ -> sixth,
      '⚄ -> sixth,
      '⚅ -> sixth))))

}