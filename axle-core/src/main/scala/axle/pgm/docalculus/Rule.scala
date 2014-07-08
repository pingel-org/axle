
package axle.pgm.docalculus

import spire.algebra.Eq
import spire.algebra.Field

abstract class Rule extends Form {

  def apply[T: Eq, N: Field](q: CausalityProbability[T, N], m: CausalModel[T, N], namer: VariableNamer[T, N]): List[Form]

}
