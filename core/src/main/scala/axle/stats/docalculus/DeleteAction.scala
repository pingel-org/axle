
package org.pingel.causality.docalculus

import scala.collection._

import org.pingel.causality.CausalModel
import org.pingel.bayes.Probability
import org.pingel.bayes.RandomVariable
import org.pingel.bayes.VariableNamer
import org.pingel.bayes.Variable
import org.pingel.gestalt.core.Form
import org.pingel.gestalt.core.Unifier

class DeleteAction extends Rule {

  def apply(q: Probability, m: CausalModel, namer: VariableNamer) = {

    val results = mutable.ListBuffer[Form]()

    val Y = q.getQuestion()
    val W = q.getGiven()

    for (z <- q.getActions()) {

      val Z = Set[Variable](z)
      val X = q.getActions() - z
      val XW = X ++ W

      val subModel = m.duplicate()
      subModel.g.removeInputs(X)

      var ancestorsOfW = Set[RandomVariable]()
      subModel.g.collectAncestors(W, ancestorsOfW)
      if (!ancestorsOfW.contains(z.getRandomVariable())) {
        subModel.g.removeInputs(Z)
      }

      if (subModel.blocks(Y, Z, XW)) {

        val Ycopy = Set[Variable]() ++ Y
        val Wcopy = Set[Variable]() ++ W

        val probFactory = new Probability(Set[Variable](), Set[Variable](), Set[Variable]())
        val unifier = new Unifier()
        unifier.put(probFactory.question, Ycopy)
        unifier.put(probFactory.given, Wcopy)
        unifier.put(probFactory.actions, X)
        val f = probFactory.createForm(unifier)
        results += f
      }
    }

    results.toList
  }

}
