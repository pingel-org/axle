package axle.game

import scala.Vector

import spire.math.Rational

import axle.stats.ConditionalProbabilityTable
import axle.stats.Variable
import axle.algebra.RegionEq

object Dice {

  def die(n: Int): ConditionalProbabilityTable[Int, Rational] = {
    import cats.implicits._
    ConditionalProbabilityTable(
      (1 to n).map(i => (RegionEq(i), Rational(1, n.toLong))).toMap,
      Variable[Int](s"D$n"))
  }

  val sixth = Rational(1, 6)

  val numberToUtfFace: Int => Symbol =
    (1 to 6).zip(Vector('⚀, '⚁, '⚂, '⚃, '⚄, '⚅)).toMap

}
