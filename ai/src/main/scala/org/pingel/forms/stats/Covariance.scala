
package org.pingel.forms.stats;

import org.pingel.gestalt.core.ComplexForm;
import org.pingel.gestalt.core.FormFactory;
import org.pingel.gestalt.core.Lambda;
import org.pingel.gestalt.core.Name;
import org.pingel.gestalt.core.SimpleForm;


class Covariance extends FormFactory {

//    RandomVariable X;
//    RandomVariable Y;
    

	val X = new Name("X")
	val Y = new Name("Y")
	val lambda = new Lambda()
	lambda.add(X, new Name("RandomVariable"))
	lambda.add(Y, new Name("RandomVariable"))

	archetype = new ComplexForm(new ComplexForm(new SimpleForm(new Name("covariance")), new SimpleForm(X)), new SimpleForm(Y), lambda)

//    public Form reduce(VariableNamer namer)
//    {
//        // E[(X - E(X))(Y - E(Y))]
//        
//        Variable x1 = X.nextVariable(namer);
//        Variable x2 = X.nextVariable(namer);
//        Variable y1 = Y.nextVariable(namer);
//        Variable y2 = Y.nextVariable(namer);
//
//        Set<Variable> outer = new HashSet<Variable>();
//        outer.add(x1);
//        outer.add(y1);
//
//        Set<Variable> inx = new HashSet<Variable>();
//        inx.add(x2);
//        
//        Set<Variable> iny = new HashSet<Variable>();
//        iny.add(y2);
//        
//        return new Expectation(outer, null,
//                new Product(
//                        new Difference(
//                                x1,
//                                new Expectation(inx, null, x2)),
//                        new Difference(
//                                y1,
//                                new Expectation(iny, null, y2))));
//    }
    
//    public Form evaluate(ProbabilityTable t, Map<Variable, Form> values, VariableNamer namer) {
//
//        Form reduced = reduce(namer);
//        return reduced.evaluate(t, values, namer);
//    }
//
//    public String toLaTeX()
//    {
//        return "\\sigma_{" + X.name + Y.name + "}";
//    }

}
