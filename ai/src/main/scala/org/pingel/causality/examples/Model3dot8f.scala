/*
 * Created on Jun 7, 2005
 *
 */
package org.pingel.causality.examples

import org.pingel.causality.CausalModel
import org.pingel.forms.Basic.PFunction
import org.pingel.bayes.RandomVariable

object Model3dot8f extends CausalModel("3.8f") {

  val X = new RandomVariable("X")
  g += X
  
  val Y = new RandomVariable("Y")
  g += Y
  
  val Z1 = new RandomVariable("Z1")
  g += Z1
  
  val Z2 = new RandomVariable("Z2")
  g += Z2
  
  val U1 = new RandomVariable("U1", None, false)
  g += U1
  
  val U2 = new RandomVariable("U2", None, false)
  g += U2

  addFunction(new PFunction(X, List(U1)))
  addFunction(new PFunction(Y, List(X, Z1, Z2, U2)))
  addFunction(new PFunction(Z1, List(X, U2)))
  addFunction(new PFunction(Z2, List(Z1, U1)))

  def main(args: Array[String]) {
    g.draw
  }

}
