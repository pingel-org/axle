
package org.pingel.causality.docalculus

import org.pingel.causality.CausalModel
import org.pingel.bayes.Probability
import org.pingel.bayes.RandomVariable
import org.pingel.bayes.VariableNamer
import org.pingel.forms.Variable
import org.pingel.gestalt.core.Form

class InsertObservation extends Rule {

    public List<Form> apply(Probability q, CausalModel m, VariableNamer namer) {

        Vector<Form> results = new Vector<Form>();

        Set<Variable> Y = q.getQuestion();
        Set<Variable> X = q.getActions();
        Set<Variable> W = q.getGiven();

        CausalModel subModel = m.duplicate();
        subModel.getGraph().removeInputs(randomVariablesOf(X));

        Set<Variable> XW = new HashSet<Variable>();
        XW.addAll(X);
        XW.addAll(W);
        
        // TODO Question: are all actions necessarily in q? Is
        // is possible to have relevant actions that are not in q?
        // I assume not.
        
        Set<RandomVariable> potentialZ = new HashSet<RandomVariable>();
        potentialZ.addAll(m.getRandomVariables());
        potentialZ.removeAll(randomVariablesOf(Y));
        potentialZ.removeAll(randomVariablesOf(X));
        potentialZ.removeAll(randomVariablesOf(W));
        
        for( RandomVariable zRandomVariable : potentialZ ) {
            
        	if( zRandomVariable.observable ) {
                
        	    Set<Variable> Z = new HashSet<Variable>();
        		Z.add(zRandomVariable.nextVariable(namer));
        		
        		if( subModel.blocks(randomVariablesOf(Y), randomVariablesOf(Z), randomVariablesOf(XW)) ) {
                    
        		    Set<Variable> ZW = Z;
        			ZW.addAll(W);
                    
                    Set<Variable> Ycopy = new HashSet<Variable>();
                    Ycopy.addAll(Y);
                    
                    Set<Variable> Xcopy = new HashSet<Variable>();
                    Xcopy.addAll(X);
                    
        			results.add(new Probability(Ycopy, ZW, Xcopy));
        		}
            }
        }
        
        return results;
    }

}
