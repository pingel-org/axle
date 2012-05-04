package org.pingel.causality.examples

import org.pingel.causality.CausalModel
import org.pingel.causality.Function
import org.pingel.bayes.RandomVariable

object Model3dot9a extends CausalModel("3.9a") {

  val X = new RandomVariable("X")
  val Y = new RandomVariable("Y")
  val U1 = new RandomVariable("U1", None, false)

  g ++= (X :: Y :: U1 :: Nil)
  
  addFunction(new Function(X, List(U1)))
  addFunction(new Function(Y, List(X, U1)))

  def main(args: Array[String]) {
    g.draw
  }

}
