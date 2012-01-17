package org.pingel.forms

object Logic {

  import org.pingel.gestalt.core.ComplexForm
  import org.pingel.gestalt.core.FormFactory
  import org.pingel.gestalt.core.Lambda
  import org.pingel.gestalt.core.Name
  import org.pingel.gestalt.core.SimpleForm

  class Entails extends FormFactory {

    val arg1 = new Name("arg1")
    val arg2 = new Name("arg2")
    val lambda = new Lambda()
    lambda.add(arg1)
    lambda.add(arg2)
    archetype = new ComplexForm(new ComplexForm(new SimpleForm(new Name("entails")), new SimpleForm(arg1)), new SimpleForm(arg2), lambda)

    //	public Type getType()
    //	{
    //		return new Function(new TupleType(new Function(new UnknownType(), new Booleans()), 
    //				new Function(new UnknownType(), new Booleans())), new Booleans());
    //	}

    //	public Form evaluate(ProbabilityTable t, Map<Variable, Form> values, VariableNamer namer)
    //	{
    //		DiscreteValue result1 = (DiscreteValue) arg0.evaluate(t, values, namer);
    //		DiscreteValue result2 = (DiscreteValue) arg1.evaluate(t, values, namer);
    //		if( result1 == Booleans.tVal && result2 == Booleans.fVal) {
    //			return Booleans.tVal;
    //		}
    //		else {
    //			
    //			return Booleans.fVal;
    //		}
    //	}

    //	public String toLaTeX()
    //	{
    //		return arg0.toLaTeX() + " \\entails " + arg1.toLaTeX();
    //	}

  }

  class Implies extends FormFactory {

    val arg1 = new Name("arg1")
    val arg2 = new Name("arg2")
    val lambda = new Lambda()
    lambda.add(arg1)
    lambda.add(arg2)

    archetype = new ComplexForm(new ComplexForm(new SimpleForm(new Name("implies")), new SimpleForm(arg1)), new SimpleForm(arg2), lambda)

    //    public Type getType()
    //    {
    //    		return new Function(new TupleType(new Function(new UnknownType(), new Booleans()), 
    //    				                         new Function(new UnknownType(), new Booleans())), new Booleans());
    //    }
    //    
    //    public Form evaluate(ProbabilityTable t, Map<Variable, Form> values, VariableNamer namer)
    //    {
    //        DiscreteValue result1 = (DiscreteValue) arg0.evaluate(t, values, namer);
    //        DiscreteValue result2 = (DiscreteValue) arg1.evaluate(t, values, namer);
    //        
    //        if( result1 == Booleans.tVal && result2 == Booleans.fVal) {
    //        		return Booleans.fVal;
    //        }
    //        else {
    //        		return Booleans.tVal;
    //        }
    //    }

    //    public String toLaTeX()
    //    {
    //        return arg0.toLaTeX() + " \\wedge " + arg1.toLaTeX();
    //    }

  }

  class ModelsOf extends FormFactory {

    val arg1 = new Name("arg1")
    val lambda = new Lambda()
    lambda.add(arg1)

    archetype = new ComplexForm(new SimpleForm(new Name("modelsOf")), new SimpleForm(arg1), lambda)

    //	public Type getType() {
    //		return new Function(new Function(new UnknownType(), new Booleans()), new Set(new Model()));
    //	}
    //
    //	public Form evaluate(ProbabilityTable t, Map<Variable, Form> values,
    //			VariableNamer namer) {
    //		// TODO
    //		return null;
    //	}
    //
    //	public String toLaTeX() {
    //		// TODO
    //		return null;
    //	}

  }

}