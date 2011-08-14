
package org.pingel.forms.stats

import org.pingel.gestalt.core.ComplexForm
import org.pingel.gestalt.core.FormFactory
import org.pingel.gestalt.core.Lambda
import org.pingel.gestalt.core.Name
import org.pingel.gestalt.core.SimpleForm

class RegressionCoefficient extends FormFactory {

//    RandomVariable X;
//    RandomVariable Y;

	val X = new Name("X")
	val Y = new Name("Y")
	val lambda = new Lambda()
	lambda.add(X, new Name("RandomVariable"))
	lambda.add(Y, new Name("RandomVariable"))

	archetype = new ComplexForm(new ComplexForm(new SimpleForm(new Name("regressionCoefficient")), new SimpleForm(X)), new SimpleForm(Y), lambda)

//    public Form reduce()
//    {
//        return new Quotient(new Covariance(X, Y), new Variance(Y));
//    }
//    
//    public Form evaluate(ProbabilityTable t, Map<Variable, Form> values, VariableNamer namer) {
//        
//        Form reduced = reduce();
//        return reduced.evaluate(t, values, namer);
//    }
//    
//    public String toLaTeX()
//    {
//        return "r_{" + X + Y + "}";
//    }
    
}
