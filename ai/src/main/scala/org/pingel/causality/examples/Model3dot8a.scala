
package org.pingel.causality.examples;

import org.pingel.causality.CausalModel
import org.pingel.bayes.Function
import org.pingel.bayes.ModelVisualizer
import org.pingel.bayes.RandomVariable

class Model3dot8a extends CausalModel("3.8a") {

	val X = addVariable(new RandomVariable("X", "x"))
	val Y = addVariable(new RandomVariable("Y", "y"))

	addFunction(new Function(Y, X))
    
    def main(args: Array[String]) {
        val model = new Model3dot8a()
        ModelVisualizer.draw(model)
    }
    
    
}
