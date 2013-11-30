
package axle.pgm.docalculus

import axle.stats._
import CausalModel._
import spire.algebra._

abstract class Rule extends Form {

  def apply[T: Eq](q: CausalityProbability[T], m: CausalModel[T], namer: VariableNamer[T]): List[Form]

}
