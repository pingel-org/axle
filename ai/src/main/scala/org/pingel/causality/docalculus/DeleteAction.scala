
package org.pingel.causality.docalculus

import org.pingel.causality.CausalModel
import org.pingel.bayes.Probability
import org.pingel.bayes.RandomVariable
import org.pingel.bayes.VariableNamer
import org.pingel.forms.Variable
import org.pingel.gestalt.core.Form
import org.pingel.gestalt.core.Unifier

class DeleteAction extends Rule {

    public List<Form> apply(Probability q, CausalModel m, VariableNamer namer) {

        Vector<Form> results = new Vector<Form>();
        
        Set<Variable> Y = q.getQuestion();
        Set<Variable> W = q.getGiven();
            
        for( Variable z : q.getActions() ) {
        	
            Set<Variable> Z = new HashSet<Variable>();
        	Z.add(z);
        	
        	Set<Variable> X = new HashSet<Variable>();
            X.addAll(q.getActions());
        	X.remove(z);
        	
        	Set<Variable> XW = new HashSet<Variable>();
        	XW.addAll(X);
        	XW.addAll(W);
        	
        	CausalModel subModel = m.duplicate();
        	subModel.getGraph().removeInputs(randomVariablesOf(X));
        	
        	Set<RandomVariable> ancestorsOfW = new HashSet<RandomVariable>();
        	subModel.getGraph().collectAncestors(randomVariablesOf(W), ancestorsOfW);
        	if( ! ancestorsOfW.contains(z.getRandomVariable()) ) {
        		subModel.getGraph().removeInputs(randomVariablesOf(Z));
        	}
        	
            if( subModel.blocks(randomVariablesOf(Y), randomVariablesOf(Z), randomVariablesOf(XW)) ) {
                Set<Variable> Ycopy = new HashSet<Variable>();
                Ycopy.addAll(Y);
                Set<Variable> Wcopy = new HashSet<Variable>();
                Wcopy.addAll(W);
                
                Probability probFactory = new Probability();
                Unifier unifier = new Unifier();
                unifier.put(probFactory.question, Ycopy);
                unifier.put(probFactory.given, Wcopy);
                unifier.put(probFactory.actions, X);
                Form f = probFactory.createForm(unifier);
                results.add(f);
            }
        }
        
        return results;
    }

}
