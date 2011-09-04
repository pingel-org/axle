/*
 * Created on Jun 7, 2005
 *
 */
package org.pingel.causality.examples

import org.pingel.causality.CausalModel
import org.pingel.causality.Function
import org.pingel.bayes.ModelVisualizer
import org.pingel.bayes.RandomVariable

object Model3dot8f extends CausalModel("3.8f") {

  val X = addVariable(new RandomVariable("X"))
  val Y = addVariable(new RandomVariable("Y"))
  val Z1 = addVariable(new RandomVariable("Z1"))
  val Z2 = addVariable(new RandomVariable("Z2"))
  val U1 = addVariable(new RandomVariable("U1", None, false))
  val U2 = addVariable(new RandomVariable("U2", None, false))

  addFunction(new Function(X, List(U1)))
  addFunction(new Function(Y, List(X, Z1, Z2, U2)))
  addFunction(new Function(Z1, List(X, U2)))
  addFunction(new Function(Z2, List(Z1, U1)))

  def main(args: Array[String]) {
    ModelVisualizer.draw(Model3dot8f)
  }

}
