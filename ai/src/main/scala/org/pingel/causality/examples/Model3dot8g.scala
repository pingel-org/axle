/*
 * Created on Jun 7, 2005
 *
 */
package org.pingel.causality.examples

import org.pingel.causality.CausalModel
import org.pingel.ptype.PFunction
import org.pingel.bayes.ModelVisualizer
import org.pingel.bayes.RandomVariable

object Model3dot8g extends CausalModel("3.8g") {

	val X = addVariable(new RandomVariable("X"))
	val Y = addVariable(new RandomVariable("Y"))
	val Z1 = addVariable(new RandomVariable("Z1"))
	val Z2 = addVariable(new RandomVariable("Z2"))
	val Z3 = addVariable(new RandomVariable("Z3"))
	val U1 = addVariable(new RandomVariable("U1", None, false))
	val U2 = addVariable(new RandomVariable("U2", None, false))
	val U3 = addVariable(new RandomVariable("U3", None, false))
	val U4 = addVariable(new RandomVariable("U4", None, false))

	addFunction(new PFunction(X, List(Z2, U1, U2, U3)))
	addFunction(new PFunction(Y, List(Z1, Z3, U1, U4)))
	addFunction(new PFunction(Z1, List(X, Z2)))
	addFunction(new PFunction(Z2, List(U3, U4)))
	addFunction(new PFunction(Z3, List(Z2, U2)))

    def main(args: Array[String]) {
        ModelVisualizer.draw(Model3dot8g)
    }

}
