
package axle.stats.docalculus

import axle.stats._

abstract class Rule extends Form {

  def apply(q: Probability, m: Model[RandomVariable[_]], namer: VariableNamer): List[Form]

}
