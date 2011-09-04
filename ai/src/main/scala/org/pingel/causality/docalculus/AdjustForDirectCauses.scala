
package org.pingel.causality.docalculus

import org.pingel.causality.CausalModel
import org.pingel.bayes.Probability
import org.pingel.bayes.RandomVariable
import org.pingel.bayes.VariableNamer
import org.pingel.forms.Variable
import org.pingel.forms.math.Product
import org.pingel.forms.math.Sigma
import org.pingel.gestalt.core.Form
import org.pingel.gestalt.core.Unifier

class AdjustForDirectCauses extends Rule
{
	
	public Form adjustForDirectCauses(CausalModel model, Probability q, VariableNamer namer, Variable action)
	{
		// page 73, Theorem 3.2.2: Adjustment for Direct Causes
		
		Set<Variable> question = q.getQuestion();
		
		Set<RandomVariable> parents = model.getGraph().getPredecessors(action.getRandomVariable());
		
		List<Variable> parentObservations = new Vector<Variable>();
		for( RandomVariable parent : parents ) {
			if( ! parent.observable ) {
				return null;
			}
			if( question.contains(parent) ) {
				return null;
			}
			parentObservations.add(parent.nextVariable(namer));
		}
		
		Set<Variable> actions = q.getActions();
		actions.remove(action);
		
		Set<Variable> given = q.getGiven();
		given.addAll(parentObservations);
		
		
		Probability first = new Probability(question, given, actions);
		
		Set<Variable> secondQuestion = new HashSet<Variable>();
		secondQuestion.addAll(parentObservations);
		
		Probability second = new Probability(secondQuestion);
		
		Sigma sigmaFactory = new Sigma();
        Unifier unifier = new Unifier();
        unifier.bind(sa1, parentObservations);

        Product productFactory = new Product();
        Unifier productUnifier = new Unifier();
        productUnifier.bind(pa1, first);
        productUnifier.bind(pa2, second);
        Form product = productFactory.createForm(unifier);
        
        
        unifier.bind(sa2, product);
        return sigmaFactory.createForm(unifier);
	}
	
	public List<Form> apply(Probability q, CausalModel m, VariableNamer namer)
	{
		List<Form> results = new Vector<Form>();
		
		for( Variable action : q.getActions() ) {
			Form result = adjustForDirectCauses(m, q, namer, action);
			if( result != null ) {
				results.add(result);
			}
		}
		
		return results;
	}
	
}
