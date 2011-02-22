package org.pingel.forms.stats;

import org.pingel.gestalt.core.ComplexForm;
import org.pingel.gestalt.core.FormFactory;
import org.pingel.gestalt.core.Lambda;
import org.pingel.gestalt.core.Name;
import org.pingel.gestalt.core.SimpleForm;

public class Variance extends FormFactory
{
	
	// public RandomVariable rv;
    
    public Variance()
    {
		Name rv = new Name("rv");
		Lambda lambda = new Lambda();
		lambda.add(rv, new Name("RandomVariable"));

    		archetype = new ComplexForm(new SimpleForm(new Name("variance")), new SimpleForm(rv), lambda);
    }
    
//    public Form reduce(VariableNamer namer)
//    {
//        // E[(X - E(X))^2]
//        Variable observation1 = rv.nextVariable(namer);
//        Set<Variable> obsSet1 = new HashSet<Variable>();
//        obsSet1.add(observation1);
//        
//        Variable observation2 = rv.nextVariable(namer);
//        Set<Variable> obsSet2 = new HashSet<Variable>();
//        obsSet2.add(observation2);
//        
//        return new Expectation(obsSet1, null, new Square(new Difference(observation1, new Expectation(obsSet2, null, observation2))));
//    }
    
//    public Form evaluate(ProbabilityTable t, Map<Variable, Form> values, VariableNamer namer) {
//
//        // TODO not efficient since we could cache E(X)
//        
//        Form reduced = reduce(namer);
//        return reduced.evaluate(t, values, namer);
//    }
//    
//    public String toLaTeX()
//    {
//        return "\\sigma^2_{" + rv.name + "}";
//    }


}
