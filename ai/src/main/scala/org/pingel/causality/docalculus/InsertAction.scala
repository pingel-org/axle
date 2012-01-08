
package org.pingel.causality.docalculus

import org.pingel.causality.CausalModel
import org.pingel.bayes.Probability
import org.pingel.bayes.RandomVariable
import org.pingel.bayes.VariableNamer
import org.pingel.forms.Variable
import org.pingel.gestalt.core.Form
import org.pingel.gestalt.core.Unifier

import scala.collection._

class InsertAction extends Rule {
	
	def apply(q: Probability, m: CausalModel, namer: VariableNamer): List[Form] =  {
		
		var results = mutable.ListBuffer[Form]()
		
		var Y = q.getQuestion()
		var X = q.getActions()
		var W = q.getGiven()
		
		var XW = X ++ W
		
		// TODO Question: are all actions necessarily in q? Is
		// is possible to have relevant actions that are not in q?
		// I assume not.
		
		var potentialZ = mutable.Set[RandomVariable]()
		potentialZ ++= m.getRandomVariables()
		potentialZ --= randomVariablesOf(Y)
		potentialZ --= randomVariablesOf(X)
		potentialZ --= randomVariablesOf(W)
		
		
		for( zRandomVariable <- potentialZ ) {
			if( zRandomVariable.observable ) {
				val zAction = zRandomVariable.nextVariable(namer)
				val Z = Set(zAction)
				
				var subModel = m.duplicate()
				subModel.getGraph().removeInputs(randomVariablesOf(X))
				val ancestorsOfW = Set[RandomVariable]()
				subModel.getGraph().collectAncestors(randomVariablesOf(W), ancestorsOfW)
				if( ! ancestorsOfW.contains(zRandomVariable) ) {
					subModel.getGraph().removeInputs(randomVariablesOf(Z))
				}
				
				if( subModel.blocks(randomVariablesOf(Y), randomVariablesOf(Z), randomVariablesOf(XW)) ) {
					var XZ = Set[Variable]()
					XZ ++= X
					XZ += zAction
					
					var Ycopy = Set[Variable]()
					Ycopy ++= Y
					
					var Wcopy = Set[Variable]()
					Wcopy ++= W
					
					var probFactory = new Probability()
					var unifier = new Unifier()
					unifier.put(probFactory.question, Ycopy)
					unifier.put(probFactory.given, Wcopy)
					unifier.put(probFactory.actions, XZ)
					val f = probFactory.createForm(unifier)
					results += f
				}
			}
		}
		
		results.toList
	}
	
}
