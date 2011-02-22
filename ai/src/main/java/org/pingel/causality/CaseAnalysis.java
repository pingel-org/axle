package org.pingel.causality;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import org.pingel.bayes.Probability;
import org.pingel.bayes.RandomVariable;
import org.pingel.bayes.VariableNamer;
import org.pingel.forms.Variable;
import org.pingel.forms.math.Product;
import org.pingel.forms.math.Sigma;

public class CaseAnalysis {

    
    public static Sigma caseAnalyze(Probability probability, RandomVariable rv, VariableNamer namer)
    {
        Variable var = rv.nextVariable(namer);
        
        Set<Variable> firstQuestion = probability.getQuestion();
        Set<Variable> firstGiven = probability.getGiven(); // returns a copy
        firstGiven.add(var);
        Set<Variable> firstActions = probability.getActions();
        
        Probability first = new Probability(firstQuestion, firstGiven, firstActions);
        
        Set<Variable> secondQuestion = new HashSet<Variable>();
        secondQuestion.add(var);
        Set<Variable> secondGiven = probability.getGiven(); // returns a copy
        Set<Variable> secondActions = probability.getActions();
        
        Probability second = new Probability(secondQuestion, secondGiven, secondActions);
        
        List<Variable> itVars = new Vector<Variable>();
        itVars.add(var);
        
        return new Sigma(itVars, new Product(first, second));
    }
    
}
