
package org.pingel.causality.docalculus

import scala.collection._
import org.pingel.causality.CausalModel
import org.pingel.bayes.Probability
import org.pingel.bayes.VariableNamer
import org.pingel.forms.Variable
import org.pingel.gestalt.core.Form
import org.pingel.gestalt.core.Unifier

class ObservationToAction extends Rule {
	
	def apply(q: Probability, m: CausalModel, namer: VariableNamer): List[Form] = {
		
		var results = new mutable.ListBuffer[Form]()
		
		var Y = q.getQuestion()
		var X = q.getActions()
		
		for( z <- q.getGiven() ) {
			
			var Z = Set[Variable]()
			Z += z
			
			var W = Set[Variable]()
			W ++= q.getGiven()
			W -= z
			
			var subModel = m.duplicate()
			subModel.getGraph().removeInputs(randomVariablesOf(X))
			subModel.getGraph().removeOutputs(randomVariablesOf(Z))
			
			var XW = Set[Variable]()
			XW ++= X
			XW ++= W

			if( subModel.blocks(randomVariablesOf(Y), randomVariablesOf(Z), randomVariablesOf(XW)) ) {
				
				var XZ = Set[Variable]()
				XZ ++= X
				XZ += z

				var Ycopy = Set[Variable]()
				Ycopy ++= Y
				
				var probFactory = new Probability()
				var unifier = new Unifier()
				unifier.put(probFactory.question, Ycopy)
				unifier.put(probFactory.given, W)
				unifier.put(probFactory.actions, XZ)
				val f = probFactory.createForm(unifier)
				results += f
			}
		}
		
		results.toList
	}
	
}
