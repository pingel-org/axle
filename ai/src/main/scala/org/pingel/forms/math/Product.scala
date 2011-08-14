package org.pingel.forms.math;

import org.pingel.gestalt.core.ComplexForm;
import org.pingel.gestalt.core.FormFactory;
import org.pingel.gestalt.core.Lambda;
import org.pingel.gestalt.core.Name;
import org.pingel.gestalt.core.SimpleForm;

class Product extends FormFactory
{
	val arg1 = new Name("arg1")
	val arg2 = new Name("arg2")
	val lambda = new Lambda()
	lambda.add(arg1)
	lambda.add(arg2)
	archetype = new ComplexForm(new ComplexForm(new SimpleForm(new Name("/")), new SimpleForm(arg1)), new SimpleForm(arg2), lambda)

//    public Form evaluate(ProbabilityTable t, Map<Variable, Form> values, VariableNamer namer)
//    {
//        Double result = 1.0;
//        
//        for( Form e : multiplicands ) {
//            DoubleValue part = (DoubleValue) e.evaluate(t, values, namer);
//            result *= part.val;
//        }
//        return new DoubleValue(result);
//    }
//    
//    public String toString()
//    {
//        String result = "";
//        for( Form m : multiplicands ) {
//            result += m.toString();
//        }
//        return result;
//    }
//
//    public Form getMultiplicand(int i)
//    {
//    		return multiplicands.get(i);
//    }
//
//    public String toLaTeX()
//    {
//        String result = "";
//        for( Form m : multiplicands ) {
//            result += m.toLaTeX();
//        }
//        return result;
//    }

}
