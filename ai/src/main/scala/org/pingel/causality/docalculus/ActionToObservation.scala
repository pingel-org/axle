
package org.pingel.causality.docalculus


import org.pingel.causality.CausalModel
import org.pingel.bayes.Probability
import org.pingel.bayes.VariableNamer
import org.pingel.bayes.RandomVariable
import org.pingel.forms.Variable
import org.pingel.gestalt.core.Form
import org.pingel.gestalt.core.Unifier
import scala.collection._

class ActionToObservation extends Rule {

    def apply(q: Probability, m: CausalModel, namer: VariableNamer) = {

       var results = mutable.ListBuffer[Form]()

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
            subModel.getGraph().removeInputs(X)
            subModel.getGraph().removeOutputs(Z)

            // ModelVisualizer.draw(subModel)
            
            var XW = Set[Variable]()
            XW ++= W
            XW ++= X
            
            if( subModel.blocks(Y, Z, XW) ) {
              
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
                results += probFactory.createForm(unifier)
            }
        }
        
        results.toList
    }

}
