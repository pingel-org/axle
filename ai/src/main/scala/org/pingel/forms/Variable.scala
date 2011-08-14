
package org.pingel.forms

import org.pingel.bayes.RandomVariable
import org.pingel.gestalt.core.FormFactory
import org.pingel.ptype.PType

class Variable extends FormFactory
{
	val name: String
	val domain: PType
    val rv: RandomVariable

    public Variable(String name, Type domain)
    {
    		this.name = name;
    		this.domain = domain;
    }
    
    public Variable(RandomVariable rv, String name)
    {
        this.rv = rv;
        this.domain = rv.getDomain();
        this.name = name;
    }

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
