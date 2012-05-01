package org.pingel.causality.examples

import org.pingel.causality.CausalModel
import org.pingel.causality.Function
import org.pingel.bayes.RandomVariable

object Model3dot9a extends CausalModel("3.9a") {

  val X = new RandomVariable("X")
  g += X
  
  val Y = new RandomVariable("Y")
  g += Y
  
  val U1 = new RandomVariable("U1", None, false)
  g += U1

  addFunction(new Function(X, List(U1)))
  addFunction(new Function(Y, List(X, U1)))

  def main(args: Array[String]) {
    g.draw
  }

}
