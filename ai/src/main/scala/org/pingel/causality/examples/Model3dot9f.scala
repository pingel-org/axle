
package org.pingel.causality.examples

import org.pingel.causality.CausalModel
import org.pingel.forms.Basic.PFunction
import org.pingel.bayes.RandomVariable

object Model3dot9f extends CausalModel("3.9f") {

  val X = new RandomVariable("X")
  val Y = new RandomVariable("Y")
  val Z = new RandomVariable("Z")
  val U1 = new RandomVariable("U1", None, false)
  val U2 = new RandomVariable("U2", None, false)

  g ++= (X :: Y :: Z :: U1 :: U2 :: Nil)
  
  addFunction(new PFunction(X, List(U1)))
  addFunction(new PFunction(Z, List(X, U2)))
  addFunction(new PFunction(Y, List(Z, U1, U2)))

  def main(args: Array[String]) {
    g.draw
  }

}
