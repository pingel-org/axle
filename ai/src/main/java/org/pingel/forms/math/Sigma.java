
package org.pingel.forms.math;
import java.util.List;

import org.pingel.forms.Variable;
import org.pingel.gestalt.core.Form;
import org.pingel.gestalt.core.FormFactory;

public class Sigma extends FormFactory
{
    private List<Variable> iteratedVariables;
    private Form arg;

    private List<List<Form>> valuesCollections;
    
//    public Form createForm(List<Variable> itVars, Form arg)
//    {
//        this.iteratedVariables = itVars;
//        this.arg = arg;
//        
//        valuesCollections = new Vector<List<Form>>();
//
//        for( Variable var : iteratedVariables ) {
//            RandomVariable rv = var.getRandomVariable();
//            valuesCollections.add(rv.getDomain().getValues());
//        }
//    }
    
//    public Form evaluate(ProbabilityTable t, Map<Variable, Form> values, VariableNamer namer)
//    {
//        // TODO assert that the iterated variables are disjoint
//        // from the variables specified in "values"
//
//        HashMap<Variable, Form> valuesCopy = new HashMap<Variable, Form>();
//        valuesCopy.putAll(values);
//        
//        Double sum = 0.0;
//        
//        for( List<Form> tuple : new ListCrossProduct<Form>(valuesCollections) ) {
//            Iterator<Variable> varIt = iteratedVariables.iterator();
//            for( Form val : tuple ) {
//                Variable var = varIt.next();
//                valuesCopy.put(var, val);
//            }
//            DoubleValue part = (DoubleValue) arg.evaluate(t, valuesCopy, namer);
//            sum += part.val;
//        }
//        return new DoubleValue(sum);
//    }
//
//    public String toLaTeX()
//    {
//        String result = "\\Sigma_{";
//        for( Variable var : iteratedVariables ) {
//            result += var.toString();
//        }
//        result += "} " + arg.toLaTeX();
//        return result;
//    }
//    
//    public Form getExpression()
//    {
//    		return arg;
//    }

}
