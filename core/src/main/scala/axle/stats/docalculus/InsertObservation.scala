
package axle.stats.docalculus

import collection._
import axle.stats._
import axle.stats.docalculus.CausalModel._

object InsertObservation extends Rule {

  // TODO Question: are all actions necessarily in q? Is
  // is possible to have relevant actions that are not in q?
  // I assume not.

  def apply(q: CausalityProbability, m: CausalModel, namer: VariableNamer): List[Form] = {

    val Y = q.question
    val X = q.actions
    val W = q.given
    val subModel = m.duplicate()
    subModel.removeInputs(subModel.nodesFor(X))
    val XW = X ++ W

    (m.randomVariables().toSet -- Y -- X -- W).flatMap(zRandomVariable => {
      if (m.observes(zRandomVariable)) {
        val z = namer.nextVariable(zRandomVariable)
        if (subModel.blocks(Y, immutable.Set(z), XW)) {
          Some(CausalityProbability(Y, W + z, X))
        } else {
          None
        }
      } else {
        None
      }
    }).toList

  }

}
