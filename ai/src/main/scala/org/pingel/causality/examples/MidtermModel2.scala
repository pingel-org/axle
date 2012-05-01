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
  addVariable(a)

  val b = new RandomVariable("B", bools, true)
  addVariable(b)

  val c = new RandomVariable("C", bools, true)
  addVariable(c)
  addFunction(new PFunction(c, List(a, b)))

  val f = new RandomVariable("F", bools, false)
  addVariable(f)

  val d = new RandomVariable("D", bools, true)
  addVariable(d)
  addFunction(new PFunction(d, List(c, f)))

  val e = new RandomVariable("E", bools, true)
  addVariable(e)
  addFunction(new PFunction(e, List(d, f)))

  def main(args: Array[String]) {
    val distribution = new PerfectDistribution(this)
    val search = new InductiveCausation(distribution)
    val g = search.ic()
    g.draw
  }

}
