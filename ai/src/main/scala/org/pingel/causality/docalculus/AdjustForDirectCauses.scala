
package org.pingel.causality.docalculus

import scala.collection._
import org.pingel.causality.CausalModel
import org.pingel.bayes.Probability
import org.pingel.bayes.RandomVariable
import org.pingel.bayes.VariableNamer
import org.pingel.bayes.Variable
import org.pingel.forms.Math.Product
import org.pingel.forms.Math.Sigma
import org.pingel.gestalt.core.Form
import org.pingel.gestalt.core.Unifier

class AdjustForDirectCauses extends Rule {

  def adjustForDirectCauses(model: CausalModel, q: Probability, namer: VariableNamer, action: Variable) = {
    // page 73, Theorem 3.2.2: Adjustment for Direct Causes

    val question = q.getQuestion()

    val parents = model.getGraph().getPredecessors(action.getRandomVariable())

    val parentObservations = List[Variable]()
    for (parent <- parents) {
      if (!parent.observable) {
        return null
      }
      if (question.contains(parent)) {
        return null
      }
      parentObservations.add(parent.nextVariable(namer))
    }

    val actions = q.getActions() - action
    val given = q.getGiven() ++ parentObservations
    val first = new Probability(question, given, actions)

    val secondQuestion = Set[Variable]() ++ parentObservations
    val second = new Probability(secondQuestion)

    val sigmaFactory = new Sigma()
    val unifier = new Unifier()
    unifier.put(sa1, parentObservations)

    val productFactory = new Product()
    val productUnifier = new Unifier()
    productUnifier.put(pa1, first)
    productUnifier.put(pa2, second)
    val product = productFactory.createForm(unifier)

    unifier.put(sa2, product)
    sigmaFactory.createForm(unifier)
  }

  def apply(q: Probability, m: CausalModel, namer: VariableNamer) = {

    var results = new mutable.ListBuffer[Form]()
    for (action <- q.getActions()) {
      val result = adjustForDirectCauses(m, q, namer, action)
      if (result != null) {
        results += result
      }
    }
    results.toList
  }

}
