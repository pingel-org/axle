
package axle.stats.docalculus

import axle.stats._
import collection._

object InsertAction extends Rule {

  def apply(q: CausalityProbability, m: Model[RandomVariable[_]], namer: VariableNamer): List[Form] = {

    val Y = q.question
    val X = q.actions
    val W = q.given

    val XW = X ++ W

    // TODO Question: are all actions necessarily in q? Is
    // is possible to have relevant actions that are not in q?
    // I assume not.

    (m.randomVariables().toSet -- Y -- X -- W).flatMap(zRandomVariable => {
      if (zRandomVariable.observable) {
        val zAction = namer.nextVariable(zRandomVariable)
        val Z = immutable.Set(zAction)

        val subModel = m.duplicate()
        subModel.removeInputs(X)
        val ancestorsOfW = Set[RandomVariable[_]]()
        subModel.collectAncestors(W, ancestorsOfW)
        if (!ancestorsOfW.contains(zRandomVariable)) {
          subModel.removeInputs(Z)
        }

        if (subModel.blocks(Y, Z, XW)) {
          Some(CausalityProbability(Y, W, X + zAction))
        } else {
          None
        }
      } else {
        None
      }
    }).toList

  }

}
