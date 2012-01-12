
package org.pingel.causality.docalculus

import org.pingel.causality.CausalModel
import org.pingel.bayes.Probability
import org.pingel.bayes.RandomVariable
import org.pingel.bayes.VariableNamer
import org.pingel.forms.Variable
import org.pingel.gestalt.core.Form

abstract class Rule {
  
    def apply(q: Probability, m: CausalModel, namer: VariableNamer): List[Form]
    
    def randomVariablesOf(variables: Set[RandomVariable]): Set[RandomVariable] = {
        var result = Set[RandomVariable]()
        for( v <- variables ) {
            result += v // NOTE: was "v.getRandomVariable()".  I suspect I may revisit this...
        }
        result
    }
}
