package org.pingel.bayes;

import java.util.List;
import java.util.Vector;

import org.pingel.gestalt.core.Form;

public class Function
{
	protected RandomVariable rv;
	public List<RandomVariable> inputs = new Vector<RandomVariable>();
	
	public Function(RandomVariable var)
	{
		this.rv = var;
	}

    public Function(RandomVariable var, RandomVariable... args)
    {
        this.rv = var;
        for(RandomVariable arg : args) {
            inputs.add(arg);
        }
    }
	
	// The API here is that the memo may already contain a precomputed answer
	// that execute should look for first.
	// If it isn't there, execute should call a private method
    // called compute

	// Note: the checking relies on seeing if the associated value
	// is null.  This is a bad idea if we are going to allow null to be a value
	// computed by a function.  If that comes up, I'll have to modify to use
	// an out-of-band way to denote "not yet computed"
	
    final public void execute(CausalModel m, Case memo)
	{
		if( memo.valueOf(rv) == null ) {
		    for(int i=0; i < inputs.size(); i++ ) {
		        m.getFunction(inputs.get(i)).execute(m, memo);
		    }
		    Form result = compute(m, memo);
//            System.out.println("rv = " + rv + ", result = " + result);
			memo.assign(rv, result);
		}
	}

	// the inputs are guaranteed by execute to have already
	// be computed before compute is called
	
    protected Value compute(CausalModel m, Case memo)
    {
        return null;
    }
}
