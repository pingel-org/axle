
package org.pingel.forms;

import org.pingel.bayes.RandomVariable;
import org.pingel.gestalt.core.FormFactory;
import org.pingel.type.Type;

public class Variable extends FormFactory
{
	private String name;
	private Type domain;
    private RandomVariable rv;

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

    public RandomVariable getRandomVariable()
    {
        return rv;
    }
    
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
