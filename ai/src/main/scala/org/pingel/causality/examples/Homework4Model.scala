package org.pingel.causality.examples;

import org.pingel.bayes.Case;
import org.pingel.bayes.CausalModel;
import org.pingel.bayes.Domain;
import org.pingel.bayes.Function;
import org.pingel.bayes.ModelVisualizer;
import org.pingel.bayes.RandomVariable;
import org.pingel.causality.RandomBooleanFunction;
import org.pingel.gestalt.core.Form;
import org.pingel.type.Booleans;

class XorOrFunction extends Function
{
    private RandomVariable in1;
    private RandomVariable in2;
    private RandomVariable in3;

    public XorOrFunction(RandomVariable var, RandomVariable in1, RandomVariable in2, RandomVariable in3)
    {
        super(var, in1, in2, in3);
        this.in1 = in1;
        this.in2 = in2;
        this.in3 = in3;
    }

    protected Form compute(CausalModel m, Case memo)
    {
        boolean val1 = new Boolean(memo.valueOf(in1).toString()).booleanValue();
        boolean val2 = new Boolean(memo.valueOf(in2).toString()).booleanValue();
        boolean val3 = new Boolean(memo.valueOf(in3).toString()).booleanValue();

        boolean result = (val2 || val3) ^ val1;
        
        return result ?
                Booleans.tVal :
                    Booleans.fVal;
    }
        
}

public class Homework4Model extends CausalModel
{
	private double p = 0.0;
	private int k;
	
	public Homework4Model(int k, double p)
	{
        super("Homework 4 Model");
        
		this.k = k;
		this.p = p;
		
		Domain bools = new Booleans();
		
		RandomVariable oldE = null, oldEp = null, oldX = null, oldY = null;
		
		for(int i=0; i <= k; i++)
		{
			RandomVariable ei = new RandomVariable("E" + i, bools, "e" + i, false);
			addVariable(ei);
			addFunction(new RandomBooleanFunction(ei, p));

			RandomVariable epi = new RandomVariable("E'" + i, bools, "e'" + i, false);
			addVariable(epi);
			addFunction(new RandomBooleanFunction(epi, p));
			
			RandomVariable xi = new RandomVariable("X" + i, bools, "x" + i);
			addVariable(xi);
			if( i == 0 ) {
			    addFunction(new RandomBooleanFunction(xi, 0.25));
			}
			else {
			    addFunction(new XorOrFunction(xi, oldE, oldX, oldY));
			}

			RandomVariable yi = new RandomVariable("Y" + i, bools, "y" + i);
			addVariable(yi);
			if( i == 0 ) {
				addFunction(new RandomBooleanFunction(yi, 0.25));
			}
			else {
			    addFunction(new XorOrFunction(yi, oldEp, oldX, oldY));
			}
			
			oldE = ei;
			oldEp = epi;
			oldX = xi;
			oldY = yi;
		}
	}
	
	public static void main(String[] argv)
	{
        CausalModel hw4 = new Homework4Model(5, 0.2);

        ModelVisualizer.draw(hw4);
	}
	
}
