package axle.game

import scala.Vector

import axle.orderSymbols
import axle.stats.ConditionalProbabilityTable0
import axle.stats.RandomVariable
import axle.stats.RandomVariable0
import axle.stats.rationalProbabilityDist
import spire.implicits.IntAlgebra
import spire.math.Rational

object Dice {

  def die(n: Int): RandomVariable0[Int, Rational] =
    RandomVariable0(
      "d" + n,
      new ConditionalProbabilityTable0((1 to n).map(i => (i, Rational(1, n))).toMap))

  //  def die(n: Natural): RandomVariable[Natural, Rational] =
  //    RandomVariable0[Natural, Rational](
  //      "d" + n,
  //      Some((1 to n.toInt).map(i => Natural(i)).toIndexedSeq),
  //      distribution = Some(new ConditionalProbabilityTable0((1 to n.toInt).map(i => (Natural(i), Rational(1, n))).toMap)))

  val sixth = Rational(1, 6)

  val faces = Vector('⚀, '⚁, '⚂, '⚃, '⚄, '⚅)

  def utfD6: RandomVariable[Symbol, Rational] =
    RandomVariable0("UTF d6",
      new ConditionalProbabilityTable0(faces.map(_ -> sixth).toMap))

}
