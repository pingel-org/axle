
package org.pingel.causality.examples

import org.pingel.causality.CausalModel
import org.pingel.ptype.PFunction
import org.pingel.bayes.ModelVisualizer
import org.pingel.bayes.RandomVariable

object Model3dot9g extends CausalModel("3.9g") {

  val X = addVariable(new RandomVariable("X"))
  val Y = addVariable(new RandomVariable("Y"))
  val Z1 = addVariable(new RandomVariable("Z1"))
  val Z2 = addVariable(new RandomVariable("Z2"))
  val U1 = addVariable(new RandomVariable("U1", None, false))
  val U2 = addVariable(new RandomVariable("U2", None, false))
		
  addFunction(new PFunction(X, List(U1)))
  addFunction(new PFunction(Z1, List(X, U2)))
  addFunction(new PFunction(Z2, List(U1, U2)))
  addFunction(new PFunction(Y, List(Z1, Z2)))

  def main(args: Array[String]) {
	  ModelVisualizer.draw(Model3dot9g)
  }

}
