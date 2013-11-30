
package axle.pgm.docalculus

import axle.stats._
import CausalModel._
import spire.algebra._

object InsertAction extends Rule {

  def apply[T: Eq](q: CausalityProbability[T], m: CausalModel[T], namer: VariableNamer[T]): List[Form] = {

    val Y = q.question
    val X = q.actions
    val W = q.given

    val XW = X ++ W

    // TODO Question: are all actions necessarily in q? Is
    // is possible to have relevant actions that are not in q?
    // I assume not.

//    (m.randomVariables().toSet -- Y -- X -- W).flatMap(zRandomVariable => {
//      if (m.observes(zRandomVariable)) {
//        val zAction = namer.nextVariable(zRandomVariable)
//        val Z = Set(zAction)
//
//        val subModel = m.duplicate()
//        subModel.removeInputs(subModel.nodesFor(X))
//        val ancestorsOfW = subModel.collectAncestors(subModel.nodesFor(W))
//        if (!ancestorsOfW.contains(zRandomVariable)) {
//          subModel.removeInputs(subModel.nodesFor(Z))
//        }
//
//        if (subModel.blocks(Y, Z, XW)) {
//          Some(CausalityProbability(Y, W, X + zAction))
//        } else {
//          None
//        }
//      } else {
//        None
//      }
//    }).toList

    Nil // TODO
  }

}
