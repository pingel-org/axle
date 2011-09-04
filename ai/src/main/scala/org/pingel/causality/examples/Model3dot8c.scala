/*
 * Created on Jun 7, 2005
 *
 */
package org.pingel.causality.examples

import org.pingel.causality.CausalModel
import org.pingel.causality.Function
import org.pingel.bayes.ModelVisualizer
import org.pingel.bayes.RandomVariable

object Model3dot8c extends CausalModel("3.8c") {

	val X = addVariable(new RandomVariable("X"))
	val Y = addVariable(new RandomVariable("Y"))
	val Z = addVariable(new RandomVariable("Z"))
	val U = addVariable(new RandomVariable("U", None, false))

	addFunction(new Function(X, List(Z)))
	addFunction(new Function(Y, List(X, Z, U)))
	addFunction(new Function(Z, List(U)))

    def main(args: Array[String]) {
        ModelVisualizer.draw(Model3dot8c)
    }
}
