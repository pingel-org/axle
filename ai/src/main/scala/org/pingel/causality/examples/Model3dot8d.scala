
package org.pingel.causality.examples

import org.pingel.causality.CausalModel
import org.pingel.forms.Basic.PFunction
import org.pingel.bayes.RandomVariable

object Model3dot8d extends CausalModel("3.8d") {

  val X = addVariable(new RandomVariable("X"))
  val Y = addVariable(new RandomVariable("Y"))
  val Z = addVariable(new RandomVariable("Z"))
  val U = addVariable(new RandomVariable("U", None, false))

  addFunction(new PFunction(X, List(Z, U)))
  addFunction(new PFunction(Y, List(X, Z)))
  addFunction(new PFunction(Z, List(U)))

  def main(args: Array[String]) {
    g.draw
  }

}
