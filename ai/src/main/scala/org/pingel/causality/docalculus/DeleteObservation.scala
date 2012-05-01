package org.pingel.causality.docalculus

import scala.collection._
import org.pingel.causality.CausalModel
import org.pingel.bayes.Probability
import org.pingel.bayes.VariableNamer
import org.pingel.forms.Variable
import org.pingel.gestalt.core.Form
import org.pingel.gestalt.core.Unifier

class DeleteObservation extends Rule {

  def apply(q: Probability, m: CausalModel, namer: VariableNamer) = {

    val results = new mutable.ListBuffer[Form]()

    val Y = q.getQuestion()
    val X = q.getActions()
    val subModel = m.duplicate()
    subModel.getGraph().removeInputs(randomVariablesOf(X))

    for (zObservation <- q.getGiven()) {

      val Z = Set[Variable]() + zObservation
      val W = Set[Variable]() ++ q.getGiven - zObservation
      val WX = Set[Variable]() ++ W ++ X

      if (subModel.blocks(randomVariablesOf(q.getGiven()), randomVariablesOf(Z), randomVariablesOf(WX))) {

        val Ycopy = Set[Variable]() ++ Y
        val Xcopy = Set[Variable]() ++ X

        val probFactory = new Probability()
        val unifier = new Unifier()
        unifier.put(probFactory.question, Ycopy)
        unifier.put(probFactory.given, W)
        unifier.put(probFactory.actions, Xcopy)
        results += probFactory.createForm(unifier)
      }
    }

    results.toList
  }

}
