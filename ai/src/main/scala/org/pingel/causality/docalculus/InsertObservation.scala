
package org.pingel.causality.docalculus

import scala.collection._
import org.pingel.causality.CausalModel
import org.pingel.bayes.Probability
import org.pingel.bayes.RandomVariable
import org.pingel.bayes.VariableNamer
import org.pingel.forms.Variable
import org.pingel.gestalt.core.Form

class InsertObservation extends Rule {

  def apply(q: Probability, m: CausalModel, namer: VariableNamer) = {

    val results = new mutable.ListBuffer[Form]()

    val Y = q.getQuestion()
    val X = q.getActions()
    val W = q.getGiven()

    val subModel = m.duplicate()
    subModel.getGraph().removeInputs(randomVariablesOf(X))

    val XW = X ++ W

    // TODO Question: are all actions necessarily in q? Is
    // is possible to have relevant actions that are not in q?
    // I assume not.

    val potentialZ = m.getRandomVariables() -- randomVariablesOf(Y) -- randomVariablesOf(X) -- randomVariablesOf(W)

    for (zRandomVariable <- potentialZ) {
      if (zRandomVariable.observable) {
        val Z = Set[Variable](zRandomVariable.nextVariable(namer))
        if (subModel.blocks(randomVariablesOf(Y), randomVariablesOf(Z), randomVariablesOf(XW))) {
          val ZW = Z ++ W
          val Ycopy = Set[Variable]() ++ Y
          val Xcopy = Set[Variable]() ++ X
          results += new Probability(Ycopy, ZW, Xcopy)
        }
      }
    }

    results.toList
  }

}
