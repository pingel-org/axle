package org.pingel.forms

import org.pingel.gestalt.core.ComplexForm
import org.pingel.gestalt.core.FormFactory
import org.pingel.gestalt.core.Lambda
import org.pingel.gestalt.core.Name
import org.pingel.gestalt.core.SimpleForm
import org.pingel.gestalt.core.Form

object Math {

class Difference extends FormFactory {

  val arg1 = new Name("arg1")
  val arg2 = new Name("arg2")
  val lambda = new Lambda()
  lambda.add(arg1)
  lambda.add(arg2)
  archetype = new ComplexForm(new ComplexForm(new SimpleForm(new Name("-")), new SimpleForm(arg1)), new SimpleForm(arg2), lambda)

//    public Form evaluate(ProbabilityTable t,
//            Map<Variable, Form> values, VariableNamer namer) {
//        
//        DoubleValue de1 = (DoubleValue) exp0.evaluate(t, values, namer);
//        DoubleValue de2 = (DoubleValue) exp1.evaluate(t, values, namer);
//        
//        return new DoubleValue(de1.val - de2.val);
//    }
//
//    public String toLaTeX()
//    {
//        return exp1.toLaTeX() + " - " + exp2.toLaTeX();
//    }

}

class Logarithm extends FormFactory
{
	val arg1 = new Name("arg1")
	val arg2 = new Name("arg2")
	val lambda = new Lambda();
	lambda.add(arg1)
	lambda.add(arg2)
	archetype = new ComplexForm(new ComplexForm(new SimpleForm(new Name("log")), new SimpleForm(arg1)), new SimpleForm(arg2), lambda)

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

class Negation extends FormFactory
{
	val arg = new Name("arg");
	val lambda = new Lambda()
	lambda.add(arg)
	archetype = new ComplexForm(new SimpleForm(new Name("negate")), new SimpleForm(arg), lambda)

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

class Quotient extends FormFactory {

	val arg1 = new Name("arg1")
	val arg2 = new Name("arg2")
	val lambda = new Lambda()
	lambda.add(arg1)
	lambda.add(arg2)
	archetype = new ComplexForm(new ComplexForm(new SimpleForm(new Name("/")), new SimpleForm(arg1)), new SimpleForm(arg2), lambda)

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

class Sigma extends FormFactory
{
    var iteratedVariables: List[Variable]
    var arg: Form

    var valuesCollections: List[List[Form]]
    
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

class Square extends FormFactory
{
	val arg1 = new Name("arg1")
	val lambda = new Lambda()
	lambda.add(arg1)
	
	archetype = new ComplexForm(new SimpleForm(new Name("square")), new SimpleForm(arg1), lambda)

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

}
