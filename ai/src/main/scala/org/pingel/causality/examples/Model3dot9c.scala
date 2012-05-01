
package org.pingel.causality.examples

import org.pingel.causality.CausalModel
import org.pingel.forms.Basic.PFunction
import org.pingel.bayes.RandomVariable

object Model3dot9c extends CausalModel("3.9c") {

  val X = new RandomVariable("X")
  g += X
  
  val Y = new RandomVariable("Y")
  g += Y
  
  val Z = new RandomVariable("Z")
  g += Z
  
  val U1 = new RandomVariable("U1", None, false)
  g += U1

  addFunction(new PFunction(X, List(U1)))
  addFunction(new PFunction(Y, List(X, Z)))
  addFunction(new PFunction(Z, List(X, U1)))

  def main(args: Array[String]) {
    g.draw
  }

}
