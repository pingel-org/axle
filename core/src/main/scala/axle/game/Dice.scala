package axle.game

import axle.stats._
import collection._

object Dice {

  def die(n: Int) = RandomVariable0("d"+n, Some(1 to n),
    distribution = Some(new ConditionalProbabilityTable0((1 to n).map(i => (i, 1d/n)).toMap)))

  def utfD6() = RandomVariable0("UTF d6",
    Some(List('⚀, '⚁, '⚂, '⚃, '⚄, '⚅).toIndexedSeq),
    distribution = Some(new ConditionalProbabilityTable0(immutable.Map(
      '⚀ -> 1d / 6,
      '⚁ -> 1d / 6,
      '⚂ -> 1d / 6,
      '⚃ -> 1d / 6,
      '⚄ -> 1d / 6,
      '⚅ -> 1d / 6))))

}