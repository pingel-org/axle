
package org.pingel.causality.docalculus;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import org.pingel.bayes.CausalModel;
import org.pingel.bayes.Probability;
import org.pingel.bayes.VariableNamer;
import org.pingel.forms.Variable;
import org.pingel.gestalt.core.Form;
import org.pingel.gestalt.core.Unifier;

public class ObservationToAction extends Rule
{
	
	public List<Form> apply(Probability q, CausalModel m, VariableNamer namer) {
		
		Vector<Form> results = new Vector<Form>();
		
		Set<Variable> Y = q.getQuestion();
		Set<Variable> X = q.getActions();
		
		for( Variable z : q.getGiven() ) {
			
			Set<Variable> Z = new HashSet<Variable>();
			Z.add(z);
			
			Set<Variable> W = new HashSet<Variable>();
			W.addAll(q.getGiven());
			W.remove(z);
			
			CausalModel subModel = m.duplicate();
			subModel.getGraph().removeInputs(randomVariablesOf(X));
			subModel.getGraph().removeOutputs(randomVariablesOf(Z));
			
			Set<Variable> XW = new HashSet<Variable>();
			XW.addAll(X);
			XW.addAll(W);
			
			if( subModel.blocks(randomVariablesOf(Y), randomVariablesOf(Z), randomVariablesOf(XW)) ) {
				
				Set<Variable> XZ = new HashSet<Variable>();
				XZ.addAll(X);
				XZ.add(z);
				
				Set<Variable> Ycopy = new HashSet<Variable>();
				Ycopy.addAll(Y);
				
				Probability probFactory = new Probability();
				Unifier unifier = new Unifier();
				unifier.put(probFactory.question, Ycopy);
				unifier.put(probFactory.given, W);
				unifier.put(probFactory.actions, XZ);
				Form f = probFactory.createForm(unifier);
				results.add(f);
			}
		}
		
		return results;
	}
	
}
