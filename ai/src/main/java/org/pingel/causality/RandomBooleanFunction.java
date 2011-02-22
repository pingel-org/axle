
package org.pingel.causality;

import org.pingel.bayes.Case;
import org.pingel.bayes.RandomVariable;
import org.pingel.bayes.Value;
import org.pingel.type.Booleans;

public class RandomBooleanFunction extends Function
{
	private double p;

	public RandomBooleanFunction(RandomVariable var, double p)
	{
		super(var);
		this.p = p;
	}
	
	protected Value compute(CausalModel m, Case memo)
	{
	    return (Math.random() < p) ? 
	            Booleans.tVal :
	                Booleans.fVal;
	}

}
