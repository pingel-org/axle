package axle.data

import cats.implicits._
import spire.math.Rational
import axle.stats.ConditionalProbabilityTable

object Coin {

  def sides = Vector('HEAD, 'TAIL)

  def flipModel(
    pHead: Rational = Rational(1, 2)
  ): ConditionalProbabilityTable[Symbol, Rational] =
    ConditionalProbabilityTable[Symbol, Rational](
      Map(
        'HEAD -> pHead,
        'TAIL -> (1 - pHead)))

}