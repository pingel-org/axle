
package axle.stats.docalculus

import axle.stats._

abstract class Rule extends Form {

  def apply(q: CausalityProbability, m: CausalModel, namer: VariableNamer): List[Form]

}
