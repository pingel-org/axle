
package axle.pgm.docalculus

import axle.stats._
import CausalModel._
import spire.algebra._

abstract class Rule extends Form {

  def apply[T: Eq, N: Field](q: CausalityProbability[T, N], m: CausalModel[T, N], namer: VariableNamer[T, N]): List[Form]

}
