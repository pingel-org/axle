package axle.pgm.docalculus

import axle.stats._
import spire.algebra._

case class CausalityProbability[T: Eq](
  question: Set[RandomVariable[T]],
  given: Set[RandomVariable[T]],
  actions: Set[RandomVariable[T]]) extends Form
