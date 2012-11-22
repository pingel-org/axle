
package axle.stats.docalculus

import axle.stats._
import axle.stats.docalculus.CausalModel._
import collection._

object ObservationToAction extends Rule {

  def apply(q: CausalityProbability, m: CausalModel, namer: VariableNamer): List[Form] = {

    val Y = q.question
    val X = q.actions

//    q.given.flatMap(z => {
//
//      val Z = immutable.Set(z)
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
