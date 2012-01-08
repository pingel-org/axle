
package org.pingel.causality.docalculus

import scala.collection._

import org.pingel.causality.CausalModel
import org.pingel.bayes.Probability
import org.pingel.bayes.RandomVariable
import org.pingel.bayes.VariableNamer
import org.pingel.forms.Variable
import org.pingel.gestalt.core.Form
import org.pingel.gestalt.core.Unifier

class DeleteAction extends Rule {

    def apply(q: Probability, m: CausalModel, namer: VariableNamer) = {

        var results = mutable.ListBuffer[Form]()
        
        var Y = q.getQuestion()
        var W = q.getGiven()
            
        for( z <- q.getActions() ) {
        	
            var Z = Set[Variable]()
        	Z += z

        	var X = Set[Variable]()
            X ++= q.getActions()
        	X -= z
        	
        	var XW = Set[Variable]()
        	XW ++= X
        	XW ++= W
        	
        	val subModel = m.duplicate()
        	subModel.getGraph().removeInputs(randomVariablesOf(X))

        	var ancestorsOfW = Set[RandomVariable]()
        	subModel.getGraph().collectAncestors(randomVariablesOf(W), ancestorsOfW)
        	if( ! ancestorsOfW.contains(z.getRandomVariable()) ) {
        		subModel.getGraph().removeInputs(randomVariablesOf(Z))
        	}
        	
            if( subModel.blocks(randomVariablesOf(Y), randomVariablesOf(Z), randomVariablesOf(XW)) ) {
              
                var Ycopy = mutable.Set[Variable]()
                Ycopy ++= Y
                
                var Wcopy = mutable.Set[Variable]()
                Wcopy ++= W
                
                val probFactory = new Probability()
                val unifier = new Unifier()
                unifier.put(probFactory.question, Ycopy)
                unifier.put(probFactory.given, Wcopy)
                unifier.put(probFactory.actions, X)
                val f = probFactory.createForm(unifier)
                results += f
            }
        }
        
        results.toList
    }

}
