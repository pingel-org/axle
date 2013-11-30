package axle.pgm.docalculus

import axle.stats._
import axle.pgm._
import spire.algebra._
import CausalModel._

object DeleteObservation extends Rule {

  def apply[T: Eq](q: CausalityProbability[T], m: CausalModel[T], namer: VariableNamer[T]): List[Form] = {

    val Y = q.question
    val X = q.actions
//    val subModel = m.duplicate()
//    subModel.graph.removeInputs(subModel.nodesFor(X))

//    q.given.flatMap(zObservation => {
//      val W = q.given - zObservation
//      if (subModel.blocks(q.given, Set(zObservation), W ++ X)) {
//        Some(CausalityProbability(Y, W, X))
//      } else {
//        None
//      }
//    }).toList
    
    Nil // TODO
  }

}
