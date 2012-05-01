
package org.pingel.causality.examples

import org.pingel.causality.CausalModel
import org.pingel.forms.Basic.PFunction
import org.pingel.bayes.RandomVariable

object Model3dot9h extends CausalModel("3.9h") {

  val W = new RandomVariable("W")
  g += W
  
  val X = new RandomVariable("X")
  g += X
  
  val Y = new RandomVariable("Y")
  g += Y
  
  val Z = new RandomVariable("Z")
  g += Z
  
  val U1 = new RandomVariable("U1", None, false)
  g += U1
  
  val U2 = new RandomVariable("U2", None, false)
  g += U2
  
  val U3 = new RandomVariable("U3", None, false)
  g += U3
  
  val U4 = new RandomVariable("U4", None, false)
  g += U4

  addFunction(new PFunction(W, List(X, U3)))
  addFunction(new PFunction(X, List(Z, U1, U2)))
  addFunction(new PFunction(Y, List(W, U2, U4)))
  addFunction(new PFunction(Z, List(U1, U3, U4)))

  def main(args: Array[String]) {
    g.draw
  }

}
