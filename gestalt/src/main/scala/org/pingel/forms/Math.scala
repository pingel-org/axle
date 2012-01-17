package org.pingel.forms

import org.pingel.gestalt.core.ComplexForm
import org.pingel.gestalt.core.FormFactory
import org.pingel.gestalt.core.Lambda
import org.pingel.gestalt.core.Name
import org.pingel.gestalt.core.SimpleForm
import org.pingel.gestalt.core.Form
import org.pingel.axle.iterator.ListCrossProduct

class ProbabilityTable

trait ScalaForm extends Form {

  // Note: evaluate used to take "namer: VariableNamer" as the last parameter
  // this is now in core.Name singleton object

  def evaluate(t: ProbabilityTable, values: Map[Variable, Form]): Form

  def toLaTeX(): String
}


object Math {

  trait Difference extends Form {

    val exp0: ScalaForm
    val exp1: ScalaForm
    val exp2: ScalaForm
    
    def evaluate(t: ProbabilityTable, values: Map[Variable, Form]): Form = {
      val de1 = exp0.evaluate(t, values).asInstanceOf[DoubleValue]
      val de2 = exp1.evaluate(t, values).asInstanceOf[DoubleValue]
      new DoubleValue(de1.value - de2.value)
    }

    def toLaTeX() = exp1.toLaTeX() + " - " + exp2.toLaTeX()

  }

  object DifferenceFactory extends FormFactory[Difference] {
    val arg1 = new Name("arg1")
    val arg2 = new Name("arg2")
    val lambda = new Lambda()
    lambda.add(arg1)
    lambda.add(arg2)
    val archetype = new ComplexForm(new ComplexForm(new SimpleForm(new Name("-")), new SimpleForm(arg1)), new SimpleForm(arg2), lambda)
  }

  trait Logarithm extends Form {

    val base: ScalaForm
    val arg: ScalaForm
    
    def evaluate(t: ProbabilityTable, values: Map[Variable, Form]): Form = {
      val baseResult = base.evaluate(t, values).asInstanceOf[DoubleValue]
      val argResult = arg.evaluate(t, values).asInstanceOf[DoubleValue]
      new DoubleValue(scala.Math.log(argResult.value) / scala.Math.log(baseResult.value))
    }

    def toLaTeX() = "log_{" + base.toLaTeX() + "}" + arg.toLaTeX()

  }

  object LogarithmFactory extends FormFactory[Logarithm] {
    val arg1 = new Name("arg1")
    val arg2 = new Name("arg2")
    val lambda = new Lambda()
    lambda.add(arg1)
    lambda.add(arg2)
    val archetype = new ComplexForm(new ComplexForm(new SimpleForm(new Name("log")), new SimpleForm(arg1)), new SimpleForm(arg2), lambda)
  }

  trait Negation extends Form {
    
    val expression: ScalaForm
    
    def evaluate(t: ProbabilityTable, values: Map[Variable, Form]): Form = {
      val result = expression.evaluate(t, values).asInstanceOf[DoubleValue]
      new DoubleValue(-1 * result.value)
    }

    def toLaTeX() = "- " + expression.toLaTeX()
  }

  object NegationFactory extends FormFactory[Negation] {
    val arg = new Name("arg")
    val lambda = new Lambda()
    lambda.add(arg)
    val archetype = new ComplexForm(new SimpleForm(new Name("negate")), new SimpleForm(arg), lambda)
  }

  trait Product extends Form {

    val multiplicands: List[ScalaForm]
    
    def evaluate(t: ProbabilityTable, values: Map[Variable, Form]): Form = {
      var result = 1.0
      for (e <- multiplicands) {
        val part = e.evaluate(t, values).asInstanceOf[DoubleValue]
        result *= part.value
      }
      new DoubleValue(result)
    }

    override def toString() = multiplicands.map(_.toString).toList.mkString("")

    def getMultiplicand(i: Int): Form = multiplicands(i)

    def toLaTeX() = multiplicands.map(_.toLaTeX).toList.mkString("")

  }

  object ProductFactory extends FormFactory[Product] {
    val arg1 = new Name("arg1")
    val arg2 = new Name("arg2")
    val lambda = new Lambda()
    lambda.add(arg1)
    lambda.add(arg2)
    val archetype = new ComplexForm(new ComplexForm(new SimpleForm(new Name("/")), new SimpleForm(arg1)), new SimpleForm(arg2), lambda)
  }

  trait Quotient extends Form {

    val exp0: ScalaForm
    val exp1: ScalaForm
    val exp2: ScalaForm

    def evaluate(t: ProbabilityTable, values: Map[Variable, Form]): Form = {
      val de1 = exp0.evaluate(t, values).asInstanceOf[DoubleValue]
      val de2 = exp1.evaluate(t, values).asInstanceOf[DoubleValue]
      new DoubleValue(de1.value / de2.value)
    }

    def toLaTeX(): String = exp1.toLaTeX() + " / " + exp2.toLaTeX()

  }

  object QuotientFactory extends FormFactory[Quotient] {
    val arg1 = new Name("arg1")
    val arg2 = new Name("arg2")
    val lambda = new Lambda()
    lambda.add(arg1)
    lambda.add(arg2)
    val archetype = new ComplexForm(new ComplexForm(new SimpleForm(new Name("/")), new SimpleForm(arg1)), new SimpleForm(arg2), lambda)
  }

  trait Sigma extends Form {

    var iteratedVariables: List[Variable]
    var arg: ScalaForm
    var valuesCollections: List[List[Form]]

    def evaluate(t: ProbabilityTable, values: Map[Variable, Form]): Form = {
      // TODO assert that the iterated variables are disjoint
      // from the variables specified in "values"

      var valuesCopy = Map[Variable, Form]()
      valuesCopy ++= values

      var sum = 0.0

      for (tuple <- new ListCrossProduct[Form](valuesCollections)) {
        var varIt = iteratedVariables.iterator
        for (value <- tuple) {
          val variable = varIt.next
          valuesCopy += variable -> value
        }
        val part = arg.evaluate(t, valuesCopy).asInstanceOf[DoubleValue]
        sum += part.value
      }
      new DoubleValue(sum)
    }

    def toLaTeX() = "\\Sigma_{" + iteratedVariables.map(_.toString).mkString("") + "} " + arg.toLaTeX()

    def getExpression(): Form = arg

  }

  object SigmaFactory extends FormFactory[Sigma] {

    val archetype: Form = null // TODO

//    def createForm(itVars: List[Variable], arg: Form): Form = {
//      this.iteratedVariables = itVars
//      this.arg = arg
//      valuesCollections = new List[List[Form]]()
//      for (variable <- itVars) {
//        val rv = variable.getRandomVariable()
//        valuesCollections.add(rv.getDomain().getValues())
//      }
//    }

  }

  trait Square extends Form {
    
    val expression: ScalaForm
    
    def evaluate(t: ProbabilityTable, values: Map[Variable, Form]): Form = {
      val de = expression.evaluate(t, values).asInstanceOf[DoubleValue]
      new DoubleValue(de.value * de.value)
    }

    def toLaTeX() = "(" + expression.toLaTeX() + ")^2"
  }

  object SquareFactory extends FormFactory[Square] {
    val arg1 = new Name("arg1")
    val lambda = new Lambda()
    lambda.add(arg1)
    val archetype = new ComplexForm(new SimpleForm(new Name("square")), new SimpleForm(arg1), lambda)
  }

}
