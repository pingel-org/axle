package axle.game

import scala.Vector

import axle.stats.ConditionalProbabilityTable0
import axle.stats.Distribution0
import axle.stats.rationalProbabilityDist
import spire.math.Rational

object Dice {

  def die(n: Int): Distribution0[Int, Rational] =
    new ConditionalProbabilityTable0((1 to n).map(i => (i, Rational(1, n))).toMap, s"d$n")

  //  def die(n: Natural): Distribution[Natural, Rational] =
  //    Distribution0[Natural, Rational](
  //      "d" + n,
  //      Some((1 to n.toInt).map(i => Natural(i)).toIndexedSeq),
  //      distribution = Some(new ConditionalProbabilityTable0((1 to n.toInt).map(i => (Natural(i), Rational(1, n))).toMap)))

  val sixth = Rational(1, 6)

  val faces = Vector('⚀, '⚁, '⚂, '⚃, '⚄, '⚅)

  def utfD6: Distribution0[Symbol, Rational] =
    new ConditionalProbabilityTable0(faces.map(_ -> sixth).toMap, "UTF d6")

}
