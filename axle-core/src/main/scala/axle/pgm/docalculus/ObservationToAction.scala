
package axle.pgm.docalculus

import axle.stats._
import axle.pgm._
import CausalModel._
import spire.algebra._

object ObservationToAction extends Rule {

  def apply[T: Eq, N: Field](q: CausalityProbability[T, N], m: CausalModel[T, N], namer: VariableNamer[T, N]): List[Form] = {

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
