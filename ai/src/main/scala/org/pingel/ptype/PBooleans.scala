package org.pingel.ptype

import org.pingel.bayes.Domain
import org.pingel.bayes.Value

object PBooleansValues {
    val tVal = new Value("true")
    val fVal = new Value("false")
}

class PBooleans extends Domain(List(PBooleansValues.tVal, PBooleansValues.fVal)) {

}
