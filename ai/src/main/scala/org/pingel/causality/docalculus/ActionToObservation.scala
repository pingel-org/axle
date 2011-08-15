
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

public class ActionToObservation extends Rule {

    public List<Form> apply(Probability q, CausalModel m, VariableNamer namer) {

        Vector<Form> results = new Vector<Form>();

        Set<Variable> Y = q.getQuestion();
        Set<Variable> W = q.getGiven();

//        System.out.println("Y = " + Y);
//        System.out.println("W = " + W);
        
        for( Variable z : q.getActions() ) {
            
            Set<Variable> X = new HashSet<Variable>();
            X.addAll(q.getActions());
            X.remove(z);

//            System.out.println("X = " + X);
            
            HashSet<Variable> Z = new HashSet<Variable>();
            Z.add(z);

//            System.out.println("Z = " + Z);
            
            CausalModel subModel = m.duplicate();
            subModel.getGraph().removeInputs(randomVariablesOf(X));
            subModel.getGraph().removeOutputs(randomVariablesOf(Z));

//            ModelVisualizer.draw(subModel);
            
            Set<Variable> XW = new HashSet<Variable>();
            XW.addAll(W);
            XW.addAll(X);
            
            if( subModel.blocks(randomVariablesOf(Y), randomVariablesOf(Z), randomVariablesOf(XW)) ) {
                Set<Variable> ZW = new HashSet<Variable>();
                ZW.addAll(W);
                ZW.add(z);
                
                Set<Variable> Ycopy = new HashSet<Variable>();
                Ycopy.addAll(Y);
                
                Probability probFactory = new Probability();
                Unifier unifier = new Unifier();
                unifier.put(probFactory.question, Ycopy);
                unifier.put(probFactory.given, ZW);
                unifier.put(probFactory.actions, X);
                Form f = probFactory.createForm(unifier);
                results.add(f);
            }
        }
        
        return results;
    }

}
