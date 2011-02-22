package org.pingel.forms.math;

import org.pingel.gestalt.core.ComplexForm;
import org.pingel.gestalt.core.FormFactory;
import org.pingel.gestalt.core.Lambda;
import org.pingel.gestalt.core.Name;
import org.pingel.gestalt.core.SimpleForm;

public class Logarithm extends FormFactory
{
    public Logarithm()
    {
		Name arg1 = new Name("arg1");
		Name arg2 = new Name("arg2");
		Lambda lambda = new Lambda();
		lambda.add(arg1);
		lambda.add(arg2);
    		archetype = new ComplexForm(new ComplexForm(new SimpleForm(new Name("log")), new SimpleForm(arg1)), new SimpleForm(arg2), lambda);
    }
    
//    public Form evaluate(ProbabilityTable t, Map<Variable, Form> values, VariableNamer namer)
//    {
//        DoubleValue baseResult = (DoubleValue) base.evaluate(t, values, namer);
//        DoubleValue argResult = (DoubleValue) arg.evaluate(t, values, namer);
//        return new DoubleValue( Math.log10(argResult.val) / Math.log10(baseResult.val) );
//    }
//
//    public String toLaTeX()
//    {
//        return "log_{" + base.toLaTeX() + "}" + arg.toLaTeX();
//    }

}
