
package org.pingel.causality

import org.pingel.bayes.Case
import org.pingel.bayes.RandomVariable
import org.pingel.bayes.Value
import org.pingel.ptype.PBooleansValues
import org.pingel.ptype.PFunction

class RandomBooleanFunction(rv: RandomVariable, p: Double) extends PFunction(rv, Nil) {

	def compute(m: CausalModel, memo: Case) = (scala.math.random < p) match {
		case true => PBooleansValues.tVal
		case false => PBooleansValues.fVal
	}

}
