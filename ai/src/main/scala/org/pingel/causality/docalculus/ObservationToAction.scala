
package org.pingel.causality.docalculus

import scala.collection._
import org.pingel.causality.CausalModel
import org.pingel.bayes.Probability
import org.pingel.bayes.VariableNamer
import org.pingel.forms.Variable
import org.pingel.gestalt.core.Form
import org.pingel.gestalt.core.Unifier

class ObservationToAction extends Rule {

  def apply(q: Probability, m: CausalModel, namer: VariableNamer): List[Form] = {

    val results = new mutable.ListBuffer[Form]()

    val Y = q.getQuestion()
    val X = q.getActions()

    for (z <- q.getGiven()) {

      val Z = Set[Variable](z)
      val W = q.getGiven() - z

      val subModel = m.duplicate()
      subModel.getGraph().removeInputs(X)
      subModel.getGraph().removeOutputs(Z)

      val XW = X ++ W

      if (subModel.blocks(Y, Z, XW)) {

        val XZ = X + z
        val Ycopy = Set[Variable]() ++ Y

        val probFactory = new Probability()
        val unifier = new Unifier()
        unifier.put(probFactory.question, Ycopy)
        unifier.put(probFactory.given, W)
        unifier.put(probFactory.actions, XZ)
        val f = probFactory.createForm(unifier)
        results += f
      }
    }

    results.toList
  }

}
