
package axle.pgm.docalculus

import axle.stats._
import CausalModel._

abstract class Rule extends Form {

  def apply(q: CausalityProbability, m: CausalModel, namer: VariableNamer): List[Form]

}
