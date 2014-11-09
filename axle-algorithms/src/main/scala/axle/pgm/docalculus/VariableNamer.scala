package axle.pgm.docalculus

import axle.stats.Distribution
import spire.algebra.Eq
import spire.algebra.Field

class VariableNamer[T: Eq, N: Field] {

  def duplicate: VariableNamer[T, N] = ???

  def nextVariable(rv: Distribution[T, N]): Distribution[T, N] = ???

}
