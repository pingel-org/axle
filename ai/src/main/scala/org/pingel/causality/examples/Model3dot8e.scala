/*
 * Created on Jun 7, 2005
 *
 */
package org.pingel.causality.examples

import org.pingel.causality.CausalModel
import org.pingel.forms.Basic.PFunction
import org.pingel.bayes.RandomVariable

object Model3dot8e extends CausalModel("3.8e") {

  val X = new RandomVariable("X")
  g += X
  
  val Y = new RandomVariable("Y")
  g += Y
  
  val Z = new RandomVariable("Z")
  g += Z

  val U = new RandomVariable("U", None, false)
  g += U

  addFunction(new PFunction(X, List(U)))
  addFunction(new PFunction(Y, List(Z, U)))
  addFunction(new PFunction(Z, List(X)))

  def main(args: Array[String]) {
    g.draw
  }

}
