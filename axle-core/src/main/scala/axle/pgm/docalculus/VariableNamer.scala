package axle.pgm.docalculus

import axle._
import axle.stats._
import spire.algebra._

class VariableNamer[T: Eq] {

  def duplicate: VariableNamer[T] = ???

  def nextVariable(rv: RandomVariable[T]): RandomVariable[T] = ???

}
