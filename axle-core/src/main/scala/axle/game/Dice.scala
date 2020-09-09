package axle.game

import scala.Vector

import spire.math.Rational

import axle.probability.ConditionalProbabilityTable

object Dice {

  def die(n: Int): ConditionalProbabilityTable[Int, Rational] =
    ConditionalProbabilityTable(
      (1 to n).map(i => (i, Rational(1, n.toLong))).toMap)

  val sixth = Rational(1, 6)

  val numberToUtfFace: Int => Symbol =
    (1 to 6).zip(Vector(
      Symbol("⚀"),
      Symbol("⚁"),
      Symbol("⚂"),
      Symbol("⚃"),
      Symbol("⚄"),
      Symbol("⚅"))).toMap

}
