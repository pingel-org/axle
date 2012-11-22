
package axle.stats.docalculus

import axle.stats._
import axle.stats.docalculus.CausalModel._

abstract class Rule extends Form {

  def apply(q: CausalityProbability, m: CausalModel, namer: VariableNamer): List[Form]

}
