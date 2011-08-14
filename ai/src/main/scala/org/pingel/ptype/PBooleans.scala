package org.pingel.ptype

import org.pingel.bayes.Domain
import org.pingel.bayes.Value

class Booleans extends Domain {

    val tVal = new Value("true")
    val fVal = new Value("false")

    this.addValue(tVal)
    this.addValue(fVal)
    		
}
