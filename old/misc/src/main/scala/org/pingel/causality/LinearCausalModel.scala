
package org.pingel.bayes

import org.pingel.causality.CausalModel

class LinearCausalModel(source: CausalModel)
  extends CausalModel(source.name + " (linear)") {
  
  // TODO these should be deep copies
  variable2function = source.variable2function
  newVarIndex = source.newVarIndex
  name2variable = source.name2variable
  override val g = source.g
}

