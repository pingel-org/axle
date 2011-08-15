package org.pingel.causality.docalculus;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.pingel.bayes.CausalModel;
import org.pingel.bayes.Probability;
import org.pingel.bayes.VariableNamer;
import org.pingel.forms.Variable;
import org.pingel.gestalt.core.Form;
import org.pingel.gestalt.core.Unifier;

public class DeleteObservation extends Rule {

    public List<Form> apply(Probability q, CausalModel m, VariableNamer namer) {

    		List<Form> results = new ArrayList<Form>();
        
        Set<Variable> Y = q.getQuestion();
        Set<Variable> X = q.getActions();
        CausalModel subModel = m.duplicate();
        subModel.getGraph().removeInputs(randomVariablesOf(X));
        
        for( Variable zObservation : q.getGiven() ) {
            Set<Variable> Z = new HashSet<Variable>();
            Z.add(zObservation);
            
            Set<Variable> W = new HashSet<Variable>();
            W.addAll(q.getGiven());
            W.remove(zObservation);

            Set<Variable> WX = new HashSet<Variable>();
            WX.addAll(W);
            WX.addAll(X);
            
            if( subModel.blocks(randomVariablesOf(q.getGiven()), randomVariablesOf(Z), randomVariablesOf(WX)) ) {
                Set<Variable> Ycopy = new HashSet<Variable>();
                Ycopy.addAll(Y);
                Set<Variable> Xcopy = new HashSet<Variable>();
                Xcopy.addAll(X);

                Probability probFactory = new Probability();
                Unifier unifier = new Unifier();
                unifier.put(probFactory.question, Ycopy);
                unifier.put(probFactory.given, W);
                unifier.put(probFactory.actions, Xcopy);
                Form f = probFactory.createForm(unifier);
                results.add(f);
            }
        }
        
        return results;
    }

}
