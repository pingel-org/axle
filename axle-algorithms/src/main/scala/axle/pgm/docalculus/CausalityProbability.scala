package axle.pgm.docalculus

import axle.stats.Distribution
import spire.algebra.Eq
import spire.algebra.Field

case class CausalityProbability[T: Eq, N: Field](
  question: Set[Distribution[T, N]],
  given: Set[Distribution[T, N]],
  actions: Set[Distribution[T, N]]) extends Form
