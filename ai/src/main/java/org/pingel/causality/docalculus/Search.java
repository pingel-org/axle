/*
 * Created on Jun 7, 2005
 *
 */
package org.pingel.causality.docalculus;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.pingel.bayes.CausalModel;
import org.pingel.bayes.Probability;
import org.pingel.bayes.VariableNamer;
import org.pingel.causality.examples.MidtermModel1;
import org.pingel.gestalt.core.Form;

public class Search
{
    public Search()
    {
        
    }
    
    public List<Form> expand(CausalModel model, Probability quantity, VariableNamer namer)
    {
        List<Form> results = new Vector<Form>();
        
//        System.out.println("quantity: " + quantity);

        results.addAll(new DeleteObservation().apply(quantity, model, namer.duplicate()));

        results.addAll(new InsertObservation().apply(quantity, model, namer.duplicate()));

        results.addAll(new ActionToObservation().apply(quantity, model, namer.duplicate()));

        results.addAll(new ObservationToAction().apply(quantity, model, namer.duplicate()));

        results.addAll(new DeleteAction().apply(quantity, model, namer.duplicate()));

        results.addAll(new InsertAction().apply(quantity, model, namer.duplicate()));

//        results.addAll(new AdjustForDirectCauses().apply(quantity, model, namer.duplicate()));
        
        // TODO try chain rule
                
        return results;
    }
    
    private List<Form> reduce(CausalModel model, Probability quantity, VariableNamer namer, int depth, int maxDepth)
    {
    	if( depth <= maxDepth ) {
    		List<Form> next = expand(model, quantity, namer);
    		if( next != null ) {
    			
    			for( Form e : next ) {
    				for(int i=0; i < depth; i++) {
    					System.out.print("\t");
    				}
    				//System.out.println(e.toLaTeX());
    				Probability probFactory = new Probability();
    				if( probFactory.isCreatorOf(e) ) {
    					if( probFactory.getActionSize(e) == 0 ) {
    						List<Form> result = new ArrayList<Form>();
    						result.add(e);
    						return result;
    					}
    					else {
    						List<Form> pathThroughQ = reduce(model, e, namer, depth + 1, maxDepth);
    						if( pathThroughQ != null ) {
    							pathThroughQ.add(e);
    							return pathThroughQ;
    						}
    					}
    				}
    				else {
    					System.out.println("THIS CASE IS NOT HANDLED");
    					return null;
    				}
    			}
    		}
    	}
    	return null;
    }
    

    public static void main(String[] argv)
    {
        MidtermModel1 model = new MidtermModel1();
        VariableNamer namer = new VariableNamer();
        
        Probability quantity = model.getQuantity(namer);
        Search search = new Search();
        search.reduce(model, quantity, namer, 0, 2);
        
    }

}
