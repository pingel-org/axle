package axle.game

import scala.Vector

import axle.stats.ConditionalProbabilityTable0
//import axle.stats.rationalProbabilityDist
import spire.math.Rational

object Dice {

  def die(n: Int): ConditionalProbabilityTable0[Int, Rational] =
    ConditionalProbabilityTable0((1 to n).map(i => (i, Rational(1, n.toLong))).toMap)

  val sixth = Rational(1, 6)

  val numberToUtfFace: Int => Symbol =
    (1 to 6).zip(Vector('⚀, '⚁, '⚂, '⚃, '⚄, '⚅)).toMap

}
