package axle.game

import scala.Vector

import axle.stats.ConditionalProbabilityTable
import axle.stats.Variable
import spire.math.Rational

object Dice {

  def die(n: Int): ConditionalProbabilityTable[Int, Rational] =
    ConditionalProbabilityTable(
      (1 to n).map(i => (i, Rational(1, n.toLong))).toMap,
      Variable[Int](s"D$n"))

  val sixth = Rational(1, 6)

  val numberToUtfFace: Int => Symbol =
    (1 to 6).zip(Vector('⚀, '⚁, '⚂, '⚃, '⚄, '⚅)).toMap

}
