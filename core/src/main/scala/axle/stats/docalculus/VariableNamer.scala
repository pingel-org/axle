package axle.stats.docalculus

import axle.stats._

class VariableNamer {

  def duplicate(): VariableNamer = this
  
  def nextVariable(rv: RandomVariable[_]): RandomVariable[_] = null
  
}