/*
 * Created on Jun 2, 2005
 *
 */
package org.pingel.forms

import org.pingel.gestalt.core.Form
import org.pingel.gestalt.core.Name
import org.pingel.gestalt.core.SimpleForm
import org.pingel.ptype.Reals

class DoubleValue(value: Double)  {
  
    def createDoubleValue(pValue: Double) = {
        this.value = pValue
        new SimpleForm(new Name(pValue + ""))
    }
    
    def equals(other: Reals) = other match {
      case dv: DoubleValue => (dv != null) && this.value == dv.value
      case _ => false
    }

    def compareTo(other: Form) = other match {
      case dv: DoubleValue => new DoubleValue(value).compareTo(dv.value)
      case _ => -1
    }

    def toLaTeX() = value + ""

}
