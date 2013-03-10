package axle.pgm.docalculus

import axle.stats._

case class CausalityProbability(
  question: Set[RandomVariable[_]],
  given: Set[RandomVariable[_]],
  actions: Set[RandomVariable[_]]) extends Form