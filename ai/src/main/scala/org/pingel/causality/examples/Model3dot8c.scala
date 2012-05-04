/*
 * Created on Jun 7, 2005
 *
 */
package org.pingel.causality.examples

import org.pingel.causality.CausalModel
import org.pingel.forms.Basic.PFunction
import org.pingel.bayes.RandomVariable

object Model3dot8c extends CausalModel("3.8c") {

  val X = new RandomVariable("X")
  val Y = new RandomVariable("Y")
  val Z = new RandomVariable("Z")
  val U = new RandomVariable("U", None, false)

  g ++= (Z :: Y :: Z :: U :: Nil)
  
  addFunction(new PFunction(X, List(Z)))
  addFunction(new PFunction(Y, List(X, Z, U)))
  addFunction(new PFunction(Z, List(U)))

  def main(args: Array[String]) {
    g.draw
  }
}
