
package org.pingel.causality.docalculus;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import org.pingel.bayes.CausalModel;
import org.pingel.bayes.Probability;
import org.pingel.bayes.RandomVariable;
import org.pingel.bayes.VariableNamer;
import org.pingel.forms.Variable;
import org.pingel.gestalt.core.Form;
import org.pingel.gestalt.core.Unifier;

public class InsertAction extends Rule {
	
	public List<Form> apply(Probability q, CausalModel m, VariableNamer namer) {
		
		Vector<Form> results = new Vector<Form>();
		
		Set<Variable> Y = q.getQuestion();
		Set<Variable> X = q.getActions();
		Set<Variable> W = q.getGiven();
		
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
				Variable zAction = zRandomVariable.nextVariable(namer);
				Z.add(zAction);
				
				CausalModel subModel = m.duplicate();
				subModel.getGraph().removeInputs(randomVariablesOf(X));
				Set<RandomVariable> ancestorsOfW = new HashSet<RandomVariable>();
				subModel.getGraph().collectAncestors(randomVariablesOf(W), ancestorsOfW);
				if( ! ancestorsOfW.contains(zRandomVariable) ) {
					subModel.getGraph().removeInputs(randomVariablesOf(Z));
				}
				
				if( subModel.blocks(randomVariablesOf(Y), randomVariablesOf(Z), randomVariablesOf(XW)) ) {
					Set<Variable> XZ = new HashSet<Variable>();
					XZ.addAll(X);
					XZ.add(zAction);
					
					Set<Variable> Ycopy = new HashSet<Variable>();
					Ycopy.addAll(Y);
					
					Set<Variable> Wcopy = new HashSet<Variable>();
					Wcopy.addAll(W);
					
					Probability probFactory = new Probability();
					Unifier unifier = new Unifier();
					unifier.put(probFactory.question, Ycopy);
					unifier.put(probFactory.given, Wcopy);
					unifier.put(probFactory.actions, XZ);
					Form f = probFactory.createForm(unifier);
					results.add(f);
					
				}
			}
		}
		
		return results;
	}
	
}
