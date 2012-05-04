
package org.pingel.causality.examples

import org.pingel.causality.CausalModel
import org.pingel.forms.Basic.PFunction
import org.pingel.bayes.RandomVariable

object Model3dot9g extends CausalModel("3.9g") {

  val X = new RandomVariable("X")
  val Y = new RandomVariable("Y")
  val Z1 = new RandomVariable("Z1")
  val Z2 = new RandomVariable("Z2")
  val U1 = new RandomVariable("U1", None, false)
  val U2 = new RandomVariable("U2", None, false)

  g ++= (X :: Y :: Z1 :: Z2 :: U1 :: U2 :: Nil)
  
  addFunction(new PFunction(X, List(U1)))
  addFunction(new PFunction(Z1, List(X, U2)))
  addFunction(new PFunction(Z2, List(U1, U2)))
  addFunction(new PFunction(Y, List(Z1, Z2)))

  def main(args: Array[String]) {
    g.draw
  }

}
