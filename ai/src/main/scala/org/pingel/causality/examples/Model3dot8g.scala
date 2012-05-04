/*
 * Created on Jun 7, 2005
 *
 */
package org.pingel.causality.examples

import org.pingel.causality.CausalModel
import org.pingel.forms.Basic.PFunction
import org.pingel.bayes.RandomVariable

object Model3dot8g extends CausalModel("3.8g") {

  val X = new RandomVariable("X")
  val Y = new RandomVariable("Y")
  val Z1 = new RandomVariable("Z1")
  val Z2 = new RandomVariable("Z2")
  val Z3 = new RandomVariable("Z3")
  val U1 = new RandomVariable("U1", None, false)
  val U2 = new RandomVariable("U2", None, false)
  val U3 = new RandomVariable("U3", None, false)
  val U4 = new RandomVariable("U4", None, false)

  g ++= (X :: Y :: Z1 :: Z2 :: Z3 :: U1 :: U2 :: U3 :: U4 :: Nil)
  
  addFunction(new PFunction(X, List(Z2, U1, U2, U3)))
  addFunction(new PFunction(Y, List(Z1, Z3, U1, U4)))
  addFunction(new PFunction(Z1, List(X, Z2)))
  addFunction(new PFunction(Z2, List(U3, U4)))
  addFunction(new PFunction(Z3, List(Z2, U2)))

  def main(args: Array[String]) {
    g.draw
  }

}
