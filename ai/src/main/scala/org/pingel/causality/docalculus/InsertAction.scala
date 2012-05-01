
package org.pingel.causality.docalculus

import org.pingel.causality.CausalModel
import org.pingel.bayes.Probability
import org.pingel.bayes.RandomVariable
import org.pingel.bayes.VariableNamer
import org.pingel.forms.Variable
import org.pingel.gestalt.core.Form
import org.pingel.gestalt.core.Unifier

import scala.collection._

class InsertAction extends Rule {

  def apply(q: Probability, m: CausalModel, namer: VariableNamer): List[Form] = {

    var results = mutable.ListBuffer[Form]()

    val Y = q.getQuestion()
    val X = q.getActions()
    val W = q.getGiven()

    val XW = X ++ W

    // TODO Question: are all actions necessarily in q? Is
    // is possible to have relevant actions that are not in q?
    // I assume not.

    val potentialZ = Set[RandomVariable]() ++
      m.getRandomVariables() -- randomVariablesOf(Y) -- randomVariablesOf(X) -- randomVariablesOf(W)

    for (zRandomVariable <- potentialZ) {
      if (zRandomVariable.observable) {
        val zAction = zRandomVariable.nextVariable(namer)
        val Z = Set(zAction)

        var subModel = m.duplicate()
        subModel.getGraph().removeInputs(randomVariablesOf(X))
        val ancestorsOfW = Set[RandomVariable]()
        subModel.getGraph().collectAncestors(randomVariablesOf(W), ancestorsOfW)
        if (!ancestorsOfW.contains(zRandomVariable)) {
          subModel.getGraph().removeInputs(randomVariablesOf(Z))
        }

        if (subModel.blocks(randomVariablesOf(Y), randomVariablesOf(Z), randomVariablesOf(XW))) {
          val XZ = X + zAction
          val Ycopy = Set[Variable]() ++ Y
          val Wcopy = Set[Variable]() ++ W
          val probFactory = new Probability()
          val unifier = new Unifier()
          unifier.put(probFactory.question, Ycopy)
          unifier.put(probFactory.given, Wcopy)
          unifier.put(probFactory.actions, XZ)
          val f = probFactory.createForm(unifier)
          results += f
        }
      }
    }

    results.toList
  }

}
