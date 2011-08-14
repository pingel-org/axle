
package org.pingel.forms.stats;

import org.pingel.gestalt.core.ComplexForm
import org.pingel.gestalt.core.FormFactory
import org.pingel.gestalt.core.Lambda
import org.pingel.gestalt.core.Name
import org.pingel.gestalt.core.SimpleForm

class LittleSigma extends FormFactory {

//    RandomVariable X;

	val X = new Name("X")
	val lambda = new Lambda()
	lambda.add(X, new Name("RandomVariable"))
	
	archetype = new ComplexForm(new SimpleForm(new Name("littleSigma")), new SimpleForm(X), lambda)

//    public Form reduce(VariableNamer namer)
//    {
//        // TODO I'm not sure if this is right.
//        // I think I need absolute value of each term.
//        
//        Variable x1 = X.nextVariable(namer);
//        Set<Variable> xSet1 = new HashSet<Variable>();
//        xSet1.add(x1);
//        
//        Variable x2 = X.nextVariable(namer);
//        Set<Variable> xSet2 = new HashSet<Variable>();
//        xSet2.add(x2);
//
//
//        return new Expectation(xSet1, null, new Difference(x1, new Expectation(xSet2, null, x2)));
//    }
//    
//    public Form evaluate(ProbabilityTable t,
//            Map<Variable, Form> values, VariableNamer namer)
//    {
//        // TODO would be more efficient not to recalculate E(X) for every value of x
//        Form reduced = reduce(namer);
//        return reduced.evaluate(t, values, namer);
//    }
//
//    public String toLaTeX()
//    {
//        return "\\sigma_{" + X + "}";
//    }

}
