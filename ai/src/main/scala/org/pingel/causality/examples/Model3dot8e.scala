/*
 * Created on Jun 7, 2005
 *
 */
package org.pingel.causality.examples

import org.pingel.causality.CausalModel
import org.pingel.forms.Basic.PFunction
import org.pingel.bayes.RandomVariable

object Model3dot8e extends CausalModel("3.8e") {

  val X = new RandomVariable("X")
  val Y = new RandomVariable("Y")
  val Z = new RandomVariable("Z")
  val U = new RandomVariable("U", None, false)

  g ++= (X :: Y :: Z :: U :: Nil)
  
  addFunction(new PFunction(X, List(U)))
  addFunction(new PFunction(Y, List(Z, U)))
  addFunction(new PFunction(Z, List(X)))

  def main(args: Array[String]) {
    g.draw
  }

}
