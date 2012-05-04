
package org.pingel.causality.examples

import org.pingel.causality.CausalModel
import org.pingel.forms.Basic.PFunction
import org.pingel.bayes.RandomVariable

object Model3dot9c extends CausalModel("3.9c") {

  val X = new RandomVariable("X")
  val Y = new RandomVariable("Y")
  val Z = new RandomVariable("Z")
  val U1 = new RandomVariable("U1", None, false)

  g ++= (X :: Y :: Z :: U1 :: Nil)
  
  addFunction(new PFunction(X, List(U1)))
  addFunction(new PFunction(Y, List(X, Z)))
  addFunction(new PFunction(Z, List(X, U1)))

  def main(args: Array[String]) {
    g.draw
  }

}
