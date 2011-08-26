
package org.pingel.causality

import org.pingel.bayes.Case
import org.pingel.bayes.RandomVariable
import org.pingel.bayes.Value
import org.pingel.ptype.Booleans

class RandomBooleanFunction(rv: RandomVariable, p: Double) extends Function(rv, Nil) {

	def compute(m: CausalModel, memo: Case) = (Math.random() < p) match {
		case true => Booleans.tVal
		case false => Booleans.fVal
	}

}
