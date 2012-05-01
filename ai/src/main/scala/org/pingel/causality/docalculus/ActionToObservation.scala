
package org.pingel.causality.docalculus

import org.pingel.causality.CausalModel
import org.pingel.bayes.Probability
import org.pingel.bayes.VariableNamer
import org.pingel.bayes.RandomVariable
import org.pingel.bayes.Variable
import org.pingel.gestalt.core.Form
import org.pingel.gestalt.core.Unifier
import scala.collection._

class ActionToObservation extends Rule {

  def apply(q: Probability, m: CausalModel, namer: VariableNamer) = {

    val results = mutable.ListBuffer[Form]()

    val Y = q.getQuestion()
    val W = q.getGiven()

    // println("Y = " + Y)
    // println("W = " + W)

    for (z <- q.getActions()) {

      val X = Set[Variable]() ++ q.getActions() - z
      // println("X = " + X)

      val Z = Set[Variable]() + z
      // println("Z = " + Z)

      val subModel = m.duplicate()
      subModel.getGraph().removeInputs(X)
      subModel.getGraph().removeOutputs(Z)
      // subModel.graph.draw

      val XW = Set[Variable]() ++ W ++ X

      if (subModel.blocks(Y, Z, XW)) {

        val ZW = Set[Variable]() ++ W + z

        val Ycopy = Set[Variable]() ++ Y

        val probFactory = new Probability()
        val unifier = new Unifier()
        unifier.put(probFactory.question, Ycopy)
        unifier.put(probFactory.given, ZW)
        unifier.put(probFactory.actions, X)
        results += probFactory.createForm(unifier)
      }
    }

    results.toList
  }

}
