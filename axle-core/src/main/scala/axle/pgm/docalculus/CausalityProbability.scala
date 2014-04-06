package axle.pgm.docalculus

import axle.stats._
import spire.algebra._

case class CausalityProbability[T: Eq, N: Field](
  question: Set[RandomVariable[T, N]],
  given: Set[RandomVariable[T, N]],
  actions: Set[RandomVariable[T, N]]) extends Form
