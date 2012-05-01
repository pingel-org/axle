
package org.pingel.causality.examples

import org.pingel.causality.CausalModel
import org.pingel.forms.Basic.PFunction
import org.pingel.bayes.RandomVariable

object Model3dot9e extends CausalModel("3.9e") {

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

  addFunction(new PFunction(X, List(Z, U1)))
  addFunction(new PFunction(Y, List(X, Z, U2)))
  addFunction(new PFunction(Z, List(U1, U2)))

  def main(args: Array[String]) {
    g.draw
  }

}
