package org.pingel.causality.examples

import org.pingel.causality.CausalModel
import org.pingel.bayes.Distribution
import org.pingel.gestalt.core.Domain
import org.pingel.forms.Basic.PFunction
import org.pingel.causality.InductiveCausation
import org.pingel.bayes.PartiallyDirectedGraph
import org.pingel.bayes.RandomVariable
import org.pingel.causality.PerfectDistribution
import org.pingel.forms.Basic.PBooleans

object MidtermModel2 extends CausalModel("Midterm Model 2") {

  val bools = Some(new PBooleans())

  val a = new RandomVariable("A", bools, true)
  val b = new RandomVariable("B", bools, true)
  val c = new RandomVariable("C", bools, true)
  val f = new RandomVariable("F", bools, false)
  val d = new RandomVariable("D", bools, true)
  val e = new RandomVariable("E", bools, true)

  g ++= (a :: b :: c :: f :: d :: e :: Nil)
  
  addFunction(new PFunction(c, List(a, b)))
  addFunction(new PFunction(d, List(c, f)))
  addFunction(new PFunction(e, List(d, f)))

  def main(args: Array[String]) {
    val distribution = new PerfectDistribution(this)
    val search = new InductiveCausation(distribution)
    val pdg = search.ic()
    pdg.draw
  }

}
