package org.pingel.causality.examples

import org.pingel.causality.CausalModel
import org.pingel.bayes.Distribution
import org.pingel.gestalt.core.Domain
import org.pingel.forms.Basic.PFunction
import org.pingel.bayes.InductiveCausation
import org.pingel.bayes.ModelVisualizer
import org.pingel.bayes.PartiallyDirectedGraph
import org.pingel.bayes.RandomVariable
import org.pingel.causality.PerfectDistribution
import org.pingel.forms.Basic.PBooleans

object MidtermModel2 extends CausalModel("Midterm Model 2") {

  val bools = Some(new PBooleans())

  val a = new RandomVariable("A", bools)
  addVariable(a)

  val b = new RandomVariable("B", bools)
  addVariable(b)

  val c = new RandomVariable("C", bools)
  addVariable(c)
  addFunction(new PFunction(c, List(a, b)))

  val f = new RandomVariable("F", bools, false)
  addVariable(f)

  val d = new RandomVariable("D", bools)
  addVariable(d)
  addFunction(new PFunction(d, List(c, f)))

  val e = new RandomVariable("E", bools)
  addVariable(e)
  addFunction(new PFunction(e, List(d, f)))

  def main(args: Array[String]) {
    val model = MidtermModel2
    ModelVisualizer.draw(model)
    val distribution = new PerfectDistribution(model)
    val search = new InductiveCausation(distribution)
    val g = search.ic()
  }

}
