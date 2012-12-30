
package org.pingel.causality

import org.pingel.bayes.Case
import org.pingel.bayes.RandomVariable
import org.pingel.gestalt.core.Value
import org.pingel.forms.Basic.PBooleansValues
import org.pingel.forms.Basic.PFunction

class RandomBooleanFunction(rv: RandomVariable, p: Double) extends PFunction(rv, Nil) {

  def compute(m: CausalModel, memo: Case) = (scala.math.random < p) match {
    case true => PBooleansValues.tVal
    case false => PBooleansValues.fVal
  }

}
