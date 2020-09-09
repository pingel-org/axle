package axle.data

import spire.math.Rational
import axle.probability.ConditionalProbabilityTable

object Coin {

  val head = Symbol("HEAD")
  val tail = Symbol("TAIL")

  def sides = Vector(head, tail)

  def flipModel(
    pHead: Rational = Rational(1, 2)
  ): ConditionalProbabilityTable[Symbol, Rational] =
    ConditionalProbabilityTable[Symbol, Rational](
      Map(
        head -> pHead,
        tail -> (1 - pHead)))

}