/*
 * Created on Jun 7, 2005
 *
 */
package org.pingel.causality.examples

import org.pingel.causality.CausalModel
import org.pingel.forms.Basic.PFunction
import org.pingel.bayes.RandomVariable

object Model3dot8b extends CausalModel("3.8b") {

  val X = new RandomVariable("X")
  g += X
  
  val Y = new RandomVariable("Y")
  g += Y
  
  val Z = new RandomVariable("Z")
  g += Z

  val U = new RandomVariable("U", None, false)
  g += U

  addFunction(new PFunction(Y, List(X, Z, U)))
  addFunction(new PFunction(Z, List(X, U)))

  def main(args: Array[String]) {
    g.draw
  }
}
