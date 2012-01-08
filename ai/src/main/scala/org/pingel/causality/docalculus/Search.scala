/*
 * Created on Jun 7, 2005
 *
 */
package org.pingel.causality.docalculus

import org.pingel.causality.CausalModel
import org.pingel.bayes.Probability
import org.pingel.bayes.VariableNamer
import org.pingel.causality.examples.MidtermModel1
import org.pingel.gestalt.core.Form

import scala.collection._

object Search {

    def main(args: Array[String]) {
        val model = MidtermModel1
        val namer = new VariableNamer()
        
        val quantity = model.getQuantity(namer) // Probability
        val search = new Search()
        search.reduce(model, quantity, namer, 0, 2)
    }

}

class Search {

    def expand(model: CausalModel, quantity: Probability, namer: VariableNamer) = {
      
        var results = List[Form]()
        
//        System.out.println("quantity: " + quantity);

        results ++= new DeleteObservation().apply(quantity, model, namer.duplicate())

        results ++= new InsertObservation().apply(quantity, model, namer.duplicate())

        results ++= new ActionToObservation().apply(quantity, model, namer.duplicate())

        results ++= new ObservationToAction().apply(quantity, model, namer.duplicate())

        results ++= new DeleteAction().apply(quantity, model, namer.duplicate())

        results ++= new InsertAction().apply(quantity, model, namer.duplicate())

//        results.addAll(new AdjustForDirectCauses().apply(quantity, model, namer.duplicate()));
        
        // TODO try chain rule
                
        results
    }
    
    def reduce( model: CausalModel, quantity: Probability, namer: VariableNamer, depth: Int, maxDepth: Int): List[Form] = {
    	if( depth <= maxDepth ) {
    		val next = expand(model, quantity, namer)
    		if( next != null ) {
    			
    			for( e <- next ) {
    				for( i <- 0 until depth ) {
    					print("\t")
    				}
    				// println(e.toLaTeX())
    				val probFactory = new Probability()
    				if( probFactory.isCreatorOf(e) ) {
    					if( probFactory.getActionSize(e) == 0 ) {
    						var result = new mutable.ListBuffer[Form]()
    						result += e
    						result.toList
    					}
    					else {
    						val pathThroughQ = reduce(model, e, namer, depth + 1, maxDepth)
    						if( pathThroughQ != null ) {
    							pathThroughQ.add(e)
    							return pathThroughQ
    						}
    					}
    				}
    				else {
    					println("THIS CASE IS NOT HANDLED")
    					return null
    				}
    			}
    		}
    	}
    	return null
    }

}
