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
  val Y = new RandomVariable("Y")
  val Z1 = new RandomVariable("Z1")
  val Z2 = new RandomVariable("Z2")
  val U1 = new RandomVariable("U1", None, false)
  val U2 = new RandomVariable("U2", None, false)

  g ++= (X :: Y :: Z1 :: Z2 :: U1 :: U2 :: Nil)
  
  addFunction(new PFunction(X, List(U1)))
  addFunction(new PFunction(Y, List(X, Z1, Z2, U2)))
  addFunction(new PFunction(Z1, List(X, U2)))
  addFunction(new PFunction(Z2, List(Z1, U1)))

  def main(args: Array[String]) {
    g.draw
  }

}
