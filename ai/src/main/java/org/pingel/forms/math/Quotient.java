/*
 * Created on May 31, 2005
 *
 */

package org.pingel.forms.math;

import org.pingel.gestalt.core.ComplexForm;
import org.pingel.gestalt.core.FormFactory;
import org.pingel.gestalt.core.Lambda;
import org.pingel.gestalt.core.Name;
import org.pingel.gestalt.core.SimpleForm;

public class Quotient extends FormFactory {
    
	public Quotient()
	{
		Name arg1 = new Name("arg1");
		Name arg2 = new Name("arg2");
		Lambda lambda = new Lambda();
		lambda.add(arg1);
		lambda.add(arg2);
		archetype = new ComplexForm(new ComplexForm(new SimpleForm(new Name("/")), new SimpleForm(arg1)), new SimpleForm(arg2), lambda);
	}
    
//    public Form evaluate(ProbabilityTable t,
//            Map<Variable, Form> values, VariableNamer namer) {
//
//        DoubleValue de1 = (DoubleValue) exp0.evaluate(t, values, namer);
//        DoubleValue de2 = (DoubleValue) exp1.evaluate(t, values, namer);
//
//        return new DoubleValue( de1.val / de2.val );
//    }
//
//    public String toLaTeX()
//    {
//        return exp1.toLaTeX() + " / " + exp2.toLaTeX();
//    }

}
