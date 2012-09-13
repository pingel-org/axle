
package axle.stats.docalculus

import collection._
import axle.stats._

object ActionToObservation extends Rule {

  def apply(q: CausalityProbability, m: Model[RandomVariable[_]], namer: VariableNamer): List[Form] = {

    val Y = q.question
    val W = q.given

    q.actions.flatMap(z => {
      val X = q.actions - z
      val Z = immutable.Set(z)
      val subModel = m.duplicate()
      subModel.removeInputs(X)
      subModel.removeOutputs(Z)
      if (subModel.blocks(Y, Z, W ++ X)) {
        Some(CausalityProbability(Y, W + z, X))
      } else {
        None
      }
    }).toList
  }

}
