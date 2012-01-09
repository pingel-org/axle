
package org.pingel.forms

import org.pingel.gestalt.core.FormFactory
import org.pingel.ptype.PType

trait Variable extends PType {
  def getName(): String
}

/*
class Variable(name: String) extends FormFactory {}

class DVariable(name: String, domain: PType) extends Variable(name) {}

class RVVariable(name: String, rv: RandomVariable) extends Variable(name) {

	val domain = rv.getDomain()
	
    def getRandomVariable() = rv
    
//    public String toLaTeX()
//    {
//        return name;
//    }
//
//    public Form evaluate(ProbabilityTable t, Map<Variable, Form> values, VariableNamer namer) {
//
//        return values.get(this);
//    }

}

*/
