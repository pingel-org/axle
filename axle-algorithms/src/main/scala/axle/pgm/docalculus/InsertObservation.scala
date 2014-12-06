
package axle.pgm.docalculus

import spire.algebra.Eq
import spire.algebra.Field

object InsertObservation extends Rule {

  // TODO Question: are all actions necessarily in q? Is
  // is possible to have relevant actions that are not in q?
  // I assume not.

  def apply[T: Eq, N: Field, DG[_, _]](q: CausalityProbability[T, N], m: CausalModel[T, N, DG], namer: VariableNamer[T, N]): List[Form] = {

    val Y = q.question
    val X = q.actions
    val W = q.given
    val subModel = m.duplicate
    //    subModel.graph.removeInputs(subModel.nodesFor(X))
    val XW = X ++ W

    (m.randomVariables.toSet -- Y -- X -- W).flatMap(zRandomVariable => {
      if (m.observes(zRandomVariable)) {
        val z = namer.nextVariable(zRandomVariable)
        //        if (subModel.blocks(Y, Set(z), XW)) {
        if (false) {
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
