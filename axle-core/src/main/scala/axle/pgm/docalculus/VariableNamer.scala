package axle.pgm.docalculus

import axle._
import axle.stats._
import spire.algebra._

class VariableNamer[T: Eq, N: Field] {

  def duplicate: VariableNamer[T, N] = ???

  def nextVariable(rv: Distribution[T, N]): Distribution[T, N] = ???

}
