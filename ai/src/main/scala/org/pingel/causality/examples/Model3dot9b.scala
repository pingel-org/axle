
package org.pingel.causality.examples

import org.pingel.causality.CausalModel
import org.pingel.forms.Basic.PFunction
import org.pingel.bayes.RandomVariable

object Model3dot9b extends CausalModel("3.9b") {

  val X = new RandomVariable("X")
  g += X
  
  val Y = new RandomVariable("Y")
  g += Y
  
  val Z = new RandomVariable("Z")
  g += Z
  
  val U1 = new RandomVariable("U1", None, false)
  g += U1

  addFunction(new PFunction(X, List(U1)))
  addFunction(new PFunction(Z, List(X, U1)))
  addFunction(new PFunction(Y, List(Z)))

  def main(args: Array[String]) {
    g.draw
  }

}
