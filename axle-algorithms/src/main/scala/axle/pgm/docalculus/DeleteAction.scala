
package axle.pgm.docalculus

import spire.algebra.Eq
import spire.algebra.Field

object DeleteAction extends Rule {

  def apply[T: Eq, N: Field](q: CausalityProbability[T, N], m: CausalModel[T, N], namer: VariableNamer[T, N]): List[Form] = {

    val Y = q.question
    val W = q.given

//    q.actions.flatMap(z => {
//
//      val Z = Set(z)
//      val X = q.actions - z
//      val XW = X ++ W
//
//      val subModel = m.duplicate()
//      subModel.removeInputs(subModel.nodesFor(X))
//      val ancestorsOfW = subModel.collectAncestors(subModel.nodesFor(W))
//      if (!ancestorsOfW.contains(subModel.nodeFor(z))) {
//        subModel.removeInputs(subModel.nodesFor(Z))
//      }
//      if (subModel.blocks(Y, Z, XW)) {
//        Some(CausalityProbability(Y, W, X))
//      } else {
//        None
//      }
//    }).toList
    Nil // TODO
  }

}
