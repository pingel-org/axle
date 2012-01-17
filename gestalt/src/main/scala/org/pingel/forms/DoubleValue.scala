/*
 * Created on Jun 2, 2005
 *
 */
package org.pingel.forms

import org.pingel.gestalt.core.Form
import org.pingel.gestalt.core.Name
import org.pingel.gestalt.core.SimpleForm
import org.pingel.forms.Basic.PReals

case class DoubleValue(value: Double)
  extends SimpleForm(new Name(value + "")) {

  //    def createDoubleValue(pValue: Double) = {
  //        new SimpleForm(new Name(pValue + ""))
  //    }

  def equals(other: PReals) = other match {
    case dv: DoubleValue => (dv != null) && this.value == dv.value
    case _ => false
  }

  override def compareTo(other: Form) = other match {
    case dv: DoubleValue => value.compareTo(dv.value)
    case _ => -1
  }

  def toLaTeX() = value + ""

}
