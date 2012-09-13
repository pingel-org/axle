
package axle.stats.docalculus

import axle.stats._
import collection._

object DeleteAction extends Rule {

  def apply(q: CausalityProbability, m: Model[RandomVariable[_]], namer: VariableNamer): List[Form] = {

    val Y = q.question
    val W = q.given

    q.actions.flatMap(z => {

      val Z = immutable.Set(z)
      val X = q.actions - z
      val XW = X ++ W

      val subModel = m.duplicate()
      subModel.removeInputs(X)

      var ancestorsOfW = Set[RandomVariable[_]]()
      subModel.collectAncestors(W, ancestorsOfW)
      if (!ancestorsOfW.contains(z)) {
        subModel.removeInputs(Z)
      }
      if (subModel.blocks(Y, Z, XW)) {
        Some(CausalityProbability(Y, W, X))
      } else {
        None
      }
    }).toList
  }

}
