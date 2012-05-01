
package org.pingel.causality.examples;

import org.pingel.causality.CausalModel
import org.pingel.causality.Function
import org.pingel.bayes.RandomVariable

object Model3dot8a extends CausalModel("3.8a") {

  val X = new RandomVariable("X")
  g += X
  
  val Y = new RandomVariable("Y")
  g += Y

  addFunction(new Function(Y, List(X)))

  def main(args: Array[String]): Unit = {
    g.draw()
  }

}
