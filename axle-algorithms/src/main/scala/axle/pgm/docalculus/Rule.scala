
package axle.pgm.docalculus

import spire.algebra.Eq
import spire.algebra.Field

trait Rule extends Form {

  def apply[T: Eq, N: Field, DG[_, _]](q: CausalityProbability[T, N], m: CausalModel[T, N, DG], namer: VariableNamer[T, N]): List[Form]

}
