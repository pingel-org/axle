
package org.pingel.causality.docalculus


import org.pingel.causality.CausalModel
import org.pingel.bayes.Probability
import org.pingel.bayes.VariableNamer
import org.pingel.forms.Variable
import org.pingel.gestalt.core.Form
import org.pingel.gestalt.core.Unifier

class ActionToObservation extends Rule {

    def apply(q: Probability, m: CausalModel, namer: VariableNamer) = {

       var results = List[Form]()

        val Y = q.getQuestion()
        val W = q.getGiven()

        // println("Y = " + Y)
        // println("W = " + W)
        
        for( z <- q.getActions() ) {
            
            var X = Set[Variable]()
            X ++= q.getActions()
            X -= z

            // println("X = " + X)
            
            var Z = Set[Variable]()
            Z += z

            // println("Z = " + Z)
            
            val subModel = m.duplicate()
            subModel.getGraph().removeInputs(randomVariablesOf(X))
            subModel.getGraph().removeOutputs(randomVariablesOf(Z))

            // ModelVisualizer.draw(subModel)
            
            var XW = Set[Variable]()
            XW ++= W
            XW ++= X
            
            if( subModel.blocks(randomVariablesOf(Y), randomVariablesOf(Z), randomVariablesOf(XW)) ) {
              
                var ZW = Set[Variable]()
                ZW ++= W
                ZW += z
                
                var Ycopy = Set[Variable]()
                Ycopy ++= Y

                val probFactory = new Probability()
                val unifier = new Unifier()
                unifier.put(probFactory.question, Ycopy)
                unifier.put(probFactory.given, ZW)
                unifier.put(probFactory.actions, X)
                results.add(probFactory.createForm(unifier))
            }
        }
        
        results
    }

}
