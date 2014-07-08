
package axle.pgm.docalculus

import spire.algebra.Eq
import spire.algebra.Field

object ActionToObservation extends Rule {

  def apply[T: Eq, N: Field](q: CausalityProbability[T, N], m: CausalModel[T, N], namer: VariableNamer[T, N]): List[Form] = {

    val Y = q.question
    val W = q.given

//    q.actions.flatMap(z => {
//      val X = q.actions - z
//      val Z = Set(z)
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
