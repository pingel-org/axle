
package axle.pgm.docalculus

import spire.algebra.Eq
import spire.algebra.Field

object ObservationToAction extends Rule {

  def apply[T: Eq, N: Field, DG](q: CausalityProbability[T, N], m: CausalModel[T, N, DG], namer: VariableNamer[T, N]): List[Form] = {

    val Y = q.question
    val X = q.actions

    //    q.given.flatMap(z => {
    //
    //      val Z = Set(z)
    //      val W = q.given - z
    //
    //      val subModel = m.duplicate()
    //      subModel.removeInputs(subModel.nodesFor(X))
    //      subModel.removeOutputs(subModel.nodesFor(Z))
    //
    //      val XW = X ++ W
    //
    //      if (subModel.blocks(Y, Z, XW)) {
    //        Some(CausalityProbability(Y, W, X + z))
    //      } else {
    //        None
    //      }
    //    }).toList

    Nil // TODO

  }

}
