package org.pingel.causality.examples;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.pingel.bayes.CausalModel;
import org.pingel.bayes.Function;
import org.pingel.bayes.ModelVisualizer;
import org.pingel.bayes.Probability;
import org.pingel.bayes.RandomVariable;
import org.pingel.bayes.VariableNamer;
import org.pingel.causality.docalculus.ActionToObservation;
import org.pingel.causality.docalculus.DeleteAction;
import org.pingel.causality.docalculus.ObservationToAction;
import org.pingel.forms.Variable;
import org.pingel.forms.math.Product;
import org.pingel.forms.math.Sigma;
import org.pingel.gestalt.core.Form;

public class SmokingModel extends CausalModel
{

    public SmokingModel()
    {
        super("Smoking Model");
        
        RandomVariable U = new RandomVariable("U", "u", false);
        addVariable(U);
        
        RandomVariable X = new RandomVariable("X", "x"); // smoke
        addVariable(X);
        addFunction(new Function(X, U));

        RandomVariable Z = new RandomVariable("Z", "z"); // tar
        addVariable(Z);
        addFunction(new Function(Z, X));

        RandomVariable Y = new RandomVariable("Y", "y"); // cancer
        addVariable(Y);
        addFunction(new Function(Y, Z, U));
    }

    private static void doTask1(CausalModel model, VariableNamer namer)
    {
        Set<Variable> question = new HashSet<Variable>();
        question.add(model.getVariable("Z").nextVariable(namer));
        Set<Variable> given = new HashSet<Variable>();
        Set<Variable> actions = new HashSet<Variable>();
        actions.add(model.getVariable("X").nextVariable(namer));
        
        Probability task1 = new Probability(question, given, actions);
        System.out.println("task1: " + task1.toString());

        List<Form> result = (new ActionToObservation()).apply(task1, model, namer);
        
        for( Form q : result ) {
        		System.out.println("after rule 2 application: " + q);
        }

    }

    private static void doTask2(CausalModel model, VariableNamer namer)
    {
        Set<Variable> question = new HashSet<Variable>();
        question.add(model.getVariable("Y").nextVariable(namer));
        Set<Variable> given = new HashSet<Variable>();
        Set<Variable> actions = new HashSet<Variable>();
        actions.add(model.getVariable("Z").nextVariable(namer));
        
        Probability task2 = new Probability(question, given, actions);
        System.out.println("task2: " + task2.toString());

//        System.out.println("Trying ActionToObservation");
//        Vector result = (new ActionToObservation()).apply(task2, model);
//        Iterator it = result.iterator();
//        while( it.hasNext() ) {
//        	Quantity q = (Quantity) it.next();
//        	System.out.println("after rule 2 application: " + q);
//        }
        
        Sigma e = task2.caseAnalysis(model.getVariable("X"), namer);
        System.out.println("after conditioning and summing over X:\n" + e);

        Product p = (Product) e.getExpression();

        Probability former = (Probability) p.getMultiplicand(0);
        System.out.println("former = " + former);
        
        List<Form> result2 = (new ActionToObservation()).apply(former, model, namer);
        for( Form q : result2 ) {
        		System.out.println("after rule ActionToObservation application: " + q);
        }
        
        
        Probability latter = (Probability) p.getMultiplicand(1);
        System.out.println("latter = " + latter);
        
        List<Form> result3 = (new DeleteAction()).apply(latter, model, namer);
        for( Form q : result3 ) {
        		System.out.println("after rule DeleteAction application: " + q);
        }
    }

    private static void doTask3(CausalModel model, VariableNamer namer)
    {
        Set<Variable> question = new HashSet<Variable>();
        question.add(model.getVariable("Y").nextVariable(namer));
        Set<Variable> given = new HashSet<Variable>();
        Set<Variable> actions = new HashSet<Variable>();
        actions.add(model.getVariable("X").nextVariable(namer));
        
        Probability task3 = new Probability(question, given, actions);
        System.out.println("task3: " + task3.toString());
        
        Sigma s = task3.caseAnalysis(model.getVariable("Z"), namer);
        System.out.println("after summing over Z:");
        System.out.println(s);
        
        Product p = (Product) s.getExpression();

        Probability former = (Probability) p.getMultiplicand(0);
        System.out.println("former = " + former);
        
        List<Form> result2 = (new ObservationToAction()).apply(former, model, namer);
        for( Form q : result2 ) {
        		System.out.println("after rule ObservationToAction application: " + q);
        }

        Probability former2 = (Probability) result2.get(0);
        System.out.println("former2 = " + former2);
        
        List<Form> result3 = (new DeleteAction()).apply(former2, model, namer);
        for( Form q : result3 ) {
        		System.out.println("after rule DeleteAction application: " + q);
        }
        
        
        Probability latter = (Probability) p.getMultiplicand(1);
        System.out.println("latter = " + latter);
        // see task 1
        
    }

    public static void main(String[] argv)
    {
    		SmokingModel model = new SmokingModel();

    		VariableNamer namer = new VariableNamer();
        
//    	doTask1(model);
//    	doTask2(model);
    		doTask3(model, namer);
        
        ModelVisualizer.draw(model);
    }

}
