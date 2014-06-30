package axle.pgm.docalculus

import axle.stats._
import spire.algebra._

case class CausalityProbability[T: Eq, N: Field](
  question: Set[Distribution[T, N]],
  given: Set[Distribution[T, N]],
  actions: Set[Distribution[T, N]]) extends Form
