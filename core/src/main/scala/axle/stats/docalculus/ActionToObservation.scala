
package axle.stats.docalculus

import collection._
import axle.stats._
import axle.stats.docalculus.CausalModel._

object ActionToObservation extends Rule {

  def apply(q: CausalityProbability, m: CausalModel, namer: VariableNamer): List[Form] = {

    val Y = q.question
    val W = q.given

//    q.actions.flatMap(z => {
//      val X = q.actions - z
//      val Z = immutable.Set(z)
//      val subModel = m.duplicate()
//      subModel.removeInputs(subModel.nodesFor(X))
//      subModel.removeOutputs(subModel.nodesFor(Z))
//      if (subModel.blocks(Y, subModel.nodesFor(Z), W ++ X)) {
//        Some(CausalityProbability(Y, W + z, X))
//      } else {
//        None
//      }
//    }).toList
    Nil
  }

}
