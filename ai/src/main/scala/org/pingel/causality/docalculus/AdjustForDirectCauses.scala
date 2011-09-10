
package org.pingel.causality.docalculus

import org.pingel.causality.CausalModel
import org.pingel.bayes.Probability
import org.pingel.bayes.RandomVariable
import org.pingel.bayes.VariableNamer
import org.pingel.forms.Variable
import org.pingel.forms.math.Product
import org.pingel.forms.math.Sigma
import org.pingel.gestalt.core.Form
import org.pingel.gestalt.core.Unifier

class AdjustForDirectCauses extends Rule {
	
	def adjustForDirectCauses(model: CausalModel, q: Probability, namer: VariableNamer, action: Variable) = {
		// page 73, Theorem 3.2.2: Adjustment for Direct Causes
		
		val question = q.getQuestion()
		
		val parents = model.getGraph().getPredecessors(action.getRandomVariable())
		
		val parentObservations = List[Variable]()
		for( parent <- parents ) {
			if( ! parent.observable ) {
				return null
			}
			if( question.contains(parent) ) {
				return null
			}
			parentObservations.add(parent.nextVariable(namer))
		}
		
		var actions = q.getActions()
		actions -= action
		
		var given = q.getGiven()
		given ++= parentObservations

		val first = new Probability(question, given, actions)
		
		var secondQuestion = Set[Variable]()
		secondQuestion ++= parentObservations
		
		val second = new Probability(secondQuestion)
		
		val sigmaFactory = new Sigma()
        val unifier = new Unifier()
        unifier.bind(sa1, parentObservations)

        val productFactory = new Product()
        val productUnifier = new Unifier()
        productUnifier.bind(pa1, first)
        productUnifier.bind(pa2, second)
        val product = productFactory.createForm(unifier)

        unifier.bind(sa2, product)
        sigmaFactory.createForm(unifier)
	}
	
	def apply(q: Probability, m: CausalModel, namer: VariableNamer) = {
	  
		var results = List[Form]()
		for( action <- q.getActions() ) {
			val result = adjustForDirectCauses(m, q, namer, action)
			if( result != null ) {
				results.add(result)
			}
		}
		results
	}
	
}
