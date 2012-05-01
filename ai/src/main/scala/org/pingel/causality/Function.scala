package org.pingel.causality

import org.pingel.bayes.Case
import org.pingel.bayes.RandomVariable
import org.pingel.gestalt.core.Form

class Function(rv: RandomVariable, inputs: List[RandomVariable] = Nil) {

  // The API here is that the memo may already contain a precomputed answer
  // that execute should look for first.
  // If it isn't there, execute should call a private method
  // called compute

  // Note: the checking relies on seeing if the associated value
  // is null.  This is a bad idea if we are going to allow null to be a value
  // computed by a function.  If that comes up, I'll have to modify to use
  // an out-of-band way to denote "not yet computed"

  def execute(m: CausalModel, memo: Case): Unit = {
    if (memo.valueOf(rv) == null) {
      for (input <- inputs) {
        m.getFunction(input).map(_.execute(m, memo))
      }
      val result = compute(m, memo)
      // println("rv = " + rv + ", result = " + result)
      memo.assign(rv, result)
    }
  }

  // the inputs are guaranteed by execute to have already
  // be computed before compute is called

  // returns "Form", not Value?:
  def compute(m: CausalModel, memo: Case): Form = null

}
