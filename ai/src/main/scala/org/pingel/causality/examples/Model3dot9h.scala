
package org.pingel.causality.examples

import org.pingel.causality.CausalModel
import org.pingel.forms.Basic.PFunction
import org.pingel.bayes.RandomVariable

object Model3dot9h extends CausalModel("3.9h") {

  val W = addVariable(new RandomVariable("W"))
  val X = addVariable(new RandomVariable("X"))
  val Y = addVariable(new RandomVariable("Y"))
  val Z = addVariable(new RandomVariable("Z"))
  val U1 = addVariable(new RandomVariable("U1", None, false))
  val U2 = addVariable(new RandomVariable("U2", None, false))
  val U3 = addVariable(new RandomVariable("U3", None, false))
  val U4 = addVariable(new RandomVariable("U4", None, false))

  addFunction(new PFunction(W, List(X, U3)))
  addFunction(new PFunction(X, List(Z, U1, U2)))
  addFunction(new PFunction(Y, List(W, U2, U4)))
  addFunction(new PFunction(Z, List(U1, U3, U4)))

  def main(args: Array[String]) {
    g.draw
  }

}
