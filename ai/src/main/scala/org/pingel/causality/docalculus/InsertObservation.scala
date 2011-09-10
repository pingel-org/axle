
package org.pingel.causality.docalculus

import org.pingel.causality.CausalModel
import org.pingel.bayes.Probability
import org.pingel.bayes.RandomVariable
import org.pingel.bayes.VariableNamer
import org.pingel.forms.Variable
import org.pingel.gestalt.core.Form

class InsertObservation extends Rule {

    def apply(q: Probability, m: CausalModel, namer: VariableNamer) = {

        var results = List[Form]()

        val Y = q.getQuestion()
        val X = q.getActions()
        val W = q.getGiven()

        val subModel = m.duplicate()
        subModel.getGraph().removeInputs(randomVariablesOf(X))

        var XW = Set[Variable]()
        XW ++= X
        XW ++= W

        // TODO Question: are all actions necessarily in q? Is
        // is possible to have relevant actions that are not in q?
        // I assume not.
        
        var potentialZ = Set[RandomVariable]()
        potentialZ ++= m.getRandomVariables()
        potentialZ --= randomVariablesOf(Y)
        potentialZ --= randomVariablesOf(X)
        potentialZ --= randomVariablesOf(W)
        
        for( zRandomVariable <- potentialZ ) {
            
        	if( zRandomVariable.observable ) {
                
        	    var Z = Set[Variable]()
        		Z += zRandomVariable.nextVariable(namer)
        		
        		if( subModel.blocks(randomVariablesOf(Y), randomVariablesOf(Z), randomVariablesOf(XW)) ) {
                    
        		    var ZW = Z
        			ZW ++= W

                    var Ycopy = Set[Variable]()
                    Ycopy ++= Y

                    var Xcopy = Set[Variable]()
                    Xcopy ++= X
                    
        			results.add(new Probability(Ycopy, ZW, Xcopy))
        		}
            }
        }
        
        results
    }

}
