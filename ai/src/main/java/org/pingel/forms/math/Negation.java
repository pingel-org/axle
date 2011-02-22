package org.pingel.forms.math;

import org.pingel.gestalt.core.ComplexForm;
import org.pingel.gestalt.core.FormFactory;
import org.pingel.gestalt.core.Lambda;
import org.pingel.gestalt.core.Name;
import org.pingel.gestalt.core.SimpleForm;

public class Negation extends FormFactory
{
	public Negation()
	{
		Name arg = new Name("arg");
		Lambda lambda = new Lambda();
		lambda.add(arg);
		archetype = new ComplexForm(new SimpleForm(new Name("negate")), new SimpleForm(arg), lambda);
	}

    
//    public Form evaluate(ProbabilityTable t, Map<Variable, Form> values, VariableNamer namer)
//    {
//        DoubleValue result = (DoubleValue) expression.evaluate(t, values, namer);
//        return new DoubleValue( -1 * result.val );
//    }
//
//    public String toLaTeX()
//    {
//        return "- " + expression.toLaTeX();
//    }
    
    
}
