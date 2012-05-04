package org.pingel.causality.examples

import org.pingel.causality.CausalModel
import org.pingel.gestalt.core.Domain
import org.pingel.causality.Function
import org.pingel.bayes.Probability
import org.pingel.bayes.RandomVariable
import org.pingel.bayes.VariableNamer
import org.pingel.forms.Variable
import org.pingel.forms.Basic.PBooleans
import org.pingel.forms.Basic.PFunction

object MidtermModel1 extends CausalModel("Midterm Model 1") {

  val bools = Some(new PBooleans())

  val U1 = new RandomVariable("U1", bools, false)
  val U2 = new RandomVariable("U2", bools, false)
  val U3 = new RandomVariable("U3", bools, false)
  val X1 = new RandomVariable("X1", bools, true)
  val X2 = new RandomVariable("X2", bools, true)
  val X3 = new RandomVariable("X3", bools, true)
  val X4 = new RandomVariable("X4", bools, true)
  val Y = new RandomVariable("Y", bools, true)

  g ++= (U1 :: U2 :: U3 :: X1 :: X2 :: X3 :: X4 :: Y :: Nil)

  addFunction(new PFunction(X1, List(U1)))
  addFunction(new PFunction(X2, List(X1, U2)))
  addFunction(new PFunction(X3, List(X2, U1, U3)))
  addFunction(new PFunction(X4, List(X3, U2)))
  addFunction(new PFunction(Y, List(X4, U3)))

  def getQuantity(namer: VariableNamer) = {
    // this returns the quantity which is involved in
    // the question: P(y|do{x1},do{x2},do{x3},do{x4})

    val question = Set(getVariable("Y").nextVariable(namer))

    val given = Set[Variable]()

    val actions = Set(
      getVariable("X1").nextVariable(namer),
      getVariable("X2").nextVariable(namer),
      getVariable("X3").nextVariable(namer),
      getVariable("X4").nextVariable(namer)
    )

    new Probability(question, given, actions)
  }

  def getClose(namer: VariableNamer) = {

    val question = Set(getVariable("Y").nextVariable(namer))

    val given = Set[Variable]()

    val actions = Set(
      getVariable("X3").nextVariable(namer),
      getVariable("X4").nextVariable(namer)
    )

    new Probability(question, given, actions)
  }

  def main(args: Array[String]): Unit = {
    g.draw
  }

}
