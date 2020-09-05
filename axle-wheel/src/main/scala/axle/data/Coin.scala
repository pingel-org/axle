package axle.data

import cats.implicits._
import spire.math.Rational
import axle.probability.ConditionalProbabilityTable

object Coin {

  def sides = Vector(Symbol("HEAD"), Symbol("TAIL"))

  def flipModel(
    pHead: Rational = Rational(1, 2)
  ): ConditionalProbabilityTable[Symbol, Rational] =
    ConditionalProbabilityTable[Symbol, Rational](
      Map(
        Symbol("HEAD") -> pHead,
        Symbol("TAIL") -> (1 - pHead)))

}