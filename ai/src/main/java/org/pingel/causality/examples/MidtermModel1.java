package org.pingel.causality.examples;

import java.util.HashSet;
import java.util.Set;

import org.pingel.bayes.CausalModel;
import org.pingel.bayes.Domain;
import org.pingel.bayes.Function;
import org.pingel.bayes.ModelVisualizer;
import org.pingel.bayes.Probability;
import org.pingel.bayes.RandomVariable;
import org.pingel.bayes.VariableNamer;
import org.pingel.forms.Variable;
import org.pingel.type.Booleans;

public class MidtermModel1 extends CausalModel
{

    public MidtermModel1()
    {
        super("Midterm Model 1");
        
        Domain bools = new Booleans();
        
		RandomVariable U1 = new RandomVariable("U1", bools, "u1", false);
		addVariable(U1);

		RandomVariable U2 = new RandomVariable("U2", bools, "u2", false);
		addVariable(U2);

		RandomVariable U3 = new RandomVariable("U3", bools, "u3", false);
		addVariable(U3);

		RandomVariable X1 = new RandomVariable("X1", bools, "x1");
		addVariable(X1);
		addFunction(new Function(X1, U1));

		RandomVariable X2 = new RandomVariable("X2", bools, "x2");
		addVariable(X2);
		addFunction(new Function(X2, X1, U2));

		RandomVariable X3 = new RandomVariable("X3", bools, "x3");
		addVariable(X3);
		addFunction(new Function(X3, X2, U1, U3));

		RandomVariable X4 = new RandomVariable("X4", bools, "x4");
		addVariable(X4);
		addFunction(new Function(X4, X3, U2));

		RandomVariable Y = new RandomVariable("Y", bools, "y");
		addVariable(Y);
		addFunction(new Function(Y, X4, U3));

    }

    public Probability getQuantity(VariableNamer namer)
    {
        // this returns the quantity which is involved in
        // the question: P(y|do{x1},do{x2},do{x3},do{x4})
        
        Set<Variable> question = new HashSet<Variable>();
        question.add(getVariable("Y").nextVariable(namer));

        Set<Variable> given = new HashSet<Variable>();
        
        Set<Variable> actions = new HashSet<Variable>();
        actions.add(getVariable("X1").nextVariable(namer));
        actions.add(getVariable("X2").nextVariable(namer));
        actions.add(getVariable("X3").nextVariable(namer));
        actions.add(getVariable("X4").nextVariable(namer));
        
        Probability result = new Probability(question, given, actions);
        
        return result;
    }

    public Probability getClose(VariableNamer namer)
    {
        Set<Variable> question = new HashSet<Variable>();
        question.add(getVariable("Y").nextVariable(namer));
        Set<Variable> given = new HashSet<Variable>();
        Set<Variable> actions = new HashSet<Variable>();
        actions.add(getVariable("X3").nextVariable(namer));
        actions.add(getVariable("X4").nextVariable(namer));
        Probability close = new Probability(question, given, actions);

        return close;
    }
    
    public static void main(String[] argv)
    {
        MidtermModel1 model = new MidtermModel1();
        ModelVisualizer.draw(model);
        
    }
    
}
