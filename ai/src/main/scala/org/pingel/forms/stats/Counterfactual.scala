package org.pingel.forms.stats;


import org.pingel.gestalt.core.ComplexForm
import org.pingel.gestalt.core.FormFactory
import org.pingel.gestalt.core.Lambda
import org.pingel.gestalt.core.Name
import org.pingel.gestalt.core.SimpleForm

class Counterfactual extends FormFactory
{

//    RandomVariable Y;
//    Variable x;

	val Y = new Name("Y")
	val x = new Name("x")
	val lambda = new Lambda()
	lambda.add(Y, new Name("RandomVariable"))
	lambda.add(x, new Name("Variable"))

	archetype = new ComplexForm(new ComplexForm(new SimpleForm(new Name("counterfactual")), new SimpleForm(Y)), new SimpleForm(x), lambda)

//    public Form evaluate(ProbabilityTable t, Map<Variable, Form> values, VariableNamer namer) {
//
////      TODO returns the "value that Y would have obtained had X been x"
//        return null;
//    }
//
//    public String toLaTeX()
//    {
//        return Y.name + "_{" + x.toLaTeX() + "}";
//    }
    
}
