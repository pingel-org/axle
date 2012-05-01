
package org.pingel.causality.examples

import org.pingel.causality.CausalModel
import org.pingel.forms.Basic.PFunction
import org.pingel.bayes.RandomVariable

object Model3dot9g extends CausalModel("3.9g") {

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
  addFunction(new PFunction(Z1, List(X, U2)))
  addFunction(new PFunction(Z2, List(U1, U2)))
  addFunction(new PFunction(Y, List(Z1, Z2)))

  def main(args: Array[String]) {
    g.draw
  }

}
