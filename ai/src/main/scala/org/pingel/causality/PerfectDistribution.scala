
package org.pingel.causality

import org.pingel.bayes.Distribution
import org.pingel.bayes.RandomVariable

class PerfectDistribution(model: CausalModel) extends Distribution(model.getRandomVariables()) {

  def separate(a: RandomVariable, b: RandomVariable): Set[RandomVariable] = {
    // TODO
    null
  }
}
