
package axle.stats.docalculus

import axle.stats._
import collection._

case class Sigma[T](xs: Set[T], f: T => Form) extends Form {

}

object AdjustForDirectCauses extends Rule {

  /**
   * page 73, Theorem 3.2.2: Adjustment for Direct Causes
   *
   */

//  def parentObservations(m: CausalModel, Xi: RandomVariable[_]): Option[immutable.Set[RandomVariable[_]]] = {
//
//    val parents = m.findVertex(_ == Xi).map(Xiv => m.predecessors(Xiv).map(_.payload)).getOrElse(Set())
//
//    parents.flatMap(parent => {
//      if (!parent.observable) {
//        None
//      } else if (q.question.contains(parent)) {
//        None
//      } else {
//        Some(namer.nextVariable(parent))
//      }
//    })
//  }
//
//  def adjustForDirectCauses(m: CausalModel, q: CausalityProbability, namer: VariableNamer, Xi: RandomVariable[_]): Option[Form] =
//    Sigma(parentObservations(m, Xi), (pai: RandomVariable[_]) => {
//      CausalityProbability(q.question, q.given ++ parentObservations, q.actions - ai) *
//        CausalityProbability(parentObservations, immutable.Set(), immutable.Set())
//    })

  def apply(q: CausalityProbability, m: CausalModel, namer: VariableNamer): List[Form] =
    Nil // TODO
//    q.actions.flatMap(adjustForDirectCauses(m, q, namer, _)).toList

}
