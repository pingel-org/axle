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

public class Square extends FormFactory
{
	public Square()
	{
		Name arg1 = new Name("arg1");
		Lambda lambda = new Lambda();
		lambda.add(arg1);

		archetype = new ComplexForm(new SimpleForm(new Name("square")), new SimpleForm(arg1), lambda);
	}
    
//    public Form evaluate(ProbabilityTable t,
//            Map<Variable, Form> values, VariableNamer namer)
//    {
//        DoubleValue de = (DoubleValue) expression.evaluate(t, values, namer);
//        
//        return new DoubleValue(de.val * de.val);
//    }
//
//    public String toLaTeX()
//    {
//        return "(" + expression.toLaTeX() + ")^2";
//    }

}
