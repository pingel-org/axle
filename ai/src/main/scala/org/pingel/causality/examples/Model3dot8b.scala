/*
 * Created on Jun 7, 2005
 *
 */
package org.pingel.causality.examples

import org.pingel.causality.CausalModel
import org.pingel.forms.Basic.PFunction
import org.pingel.bayes.RandomVariable

object Model3dot8b extends CausalModel("3.8b") {

  val X = new RandomVariable("X")
  val Y = new RandomVariable("Y")
  val Z = new RandomVariable("Z")
  val U = new RandomVariable("U", None, false)

  g ++= (X :: Y :: Z :: U :: Nil)

  addFunction(new PFunction(Y, List(X, Z, U)))
  addFunction(new PFunction(Z, List(X, U)))

  def main(args: Array[String]) {
    g.draw
  }
}
