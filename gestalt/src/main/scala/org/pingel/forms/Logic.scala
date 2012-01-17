package org.pingel.forms

import org.pingel.gestalt.core.ComplexForm
import org.pingel.gestalt.core.FormFactory
import org.pingel.gestalt.core.Lambda
import org.pingel.gestalt.core.Name
import org.pingel.gestalt.core.SimpleForm
import org.pingel.gestalt.core.Form

import org.pingel.forms.Basic.PBooleans
import org.pingel.forms.Basic.PBooleansValues

class DiscreteValue // TODO

object Logic {

  trait Entails extends ScalaForm {

    val arg0: ScalaForm
    val arg1: ScalaForm
    
//    def getType() = new Function(new TupleType(new Function(new UnknownType(), new Booleans()), 
//    				new Function(new UnknownType(), new Booleans())), new Booleans())

    def evaluate(t: ProbabilityTable, values: Map[Variable, Form]): Form = {
    	val result1 = arg0.evaluate(t, values).asInstanceOf[DiscreteValue]
    	val result2 = arg1.evaluate(t, values).asInstanceOf[DiscreteValue]
    	if( result1 == PBooleansValues.tVal && result2 == PBooleansValues.fVal) {
    		return PBooleansValues.tVal
    	}
    	else {
    		return PBooleansValues.fVal
    	}
    }

    def toLaTeX() = arg0.toLaTeX() + " \\entails " + arg1.toLaTeX()

  }
  
  object EntailsFactory extends FormFactory[Entails] {

    val arg1 = new Name("arg1")
    val arg2 = new Name("arg2")
    val lambda = new Lambda()
    lambda.add(arg1)
    lambda.add(arg2)
    val archetype = new ComplexForm(new ComplexForm(new SimpleForm(new Name("entails")), new SimpleForm(arg1)), new SimpleForm(arg2), lambda)

  }

  trait Implies extends ScalaForm {
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
  
  object ImpliesFactoryextends extends FormFactory[Implies] {

    val arg1 = new Name("arg1")
    val arg2 = new Name("arg2")
    val lambda = new Lambda()
    lambda.add(arg1)
    lambda.add(arg2)

    val archetype = new ComplexForm(new ComplexForm(new SimpleForm(new Name("implies")), new SimpleForm(arg1)), new SimpleForm(arg2), lambda)

  }

  trait ModelsOf extends ScalaForm {

//    def getType() = new PFunction(new PFunction(new UnknownType(), new Booleans()), new Set(new Model()))

   	def evaluate(t: ProbabilityTable, values: Map[Variable, Form]): Form = null // TODO

   	def toLaTeX(): String = null // TODO
  }
  
  object ModelsOf extends FormFactory[ModelsOf] {
    val arg1 = new Name("arg1")
    val lambda = new Lambda()
    lambda.add(arg1)
    val archetype = new ComplexForm(new SimpleForm(new Name("modelsOf")), new SimpleForm(arg1), lambda)
  }

}