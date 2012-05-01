/*
 * Created on Jun 7, 2005
 *
 */
package org.pingel.causality.examples

import org.pingel.causality.CausalModel
import org.pingel.forms.Basic.PFunction
import org.pingel.bayes.RandomVariable

object Model3dot8b extends CausalModel("3.8b") {

  val X = addVariable(new RandomVariable("X"))
  val Y = addVariable(new RandomVariable("Y"))
  val Z = addVariable(new RandomVariable("Z"))

  val U = addVariable(new RandomVariable("U", None, false))

  addFunction(new PFunction(Y, List(X, Z, U)))
  addFunction(new PFunction(Z, List(X, U)))

  def main(args: Array[String]) {
    g.draw
  }
}
