
package org.pingel.causality.examples;

import org.pingel.causality.CausalModel
import org.pingel.forms.Basic.PFunction
import org.pingel.bayes.ModelVisualizer
import org.pingel.bayes.RandomVariable

object Model3dot8a extends CausalModel("3.8a") {

	val X = addVariable(new RandomVariable("X"))
	val Y = addVariable(new RandomVariable("Y"))

	addFunction(new PFunction(Y, List(X)))

	// ModelVisualizer.draw(model)
    
}
