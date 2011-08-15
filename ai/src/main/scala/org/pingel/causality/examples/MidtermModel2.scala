package org.pingel.causality.examples;

import org.pingel.bayes.CausalModel;
import org.pingel.bayes.Distribution;
import org.pingel.bayes.Domain;
import org.pingel.bayes.Function;
import org.pingel.bayes.InductiveCausation;
import org.pingel.bayes.ModelVisualizer;
import org.pingel.bayes.PartiallyDirectedGraph;
import org.pingel.bayes.RandomVariable;
import org.pingel.causality.PerfectDistribution;
import org.pingel.type.Booleans;

public class MidtermModel2 extends CausalModel
{

    public MidtermModel2()
    {
        super("Midterm Model 2");
        
        Domain bools = new Booleans();
        
		RandomVariable a = new RandomVariable("A", bools, "a");
		addVariable(a);
        
		RandomVariable b = new RandomVariable("B", bools, "b");
		addVariable(b);

		RandomVariable c = new RandomVariable("C", bools, "c");
		addVariable(c);
		addFunction(new Function(c, a, b));

		RandomVariable f = new RandomVariable("F", bools, "f", false);
		addVariable(f);

		RandomVariable d = new RandomVariable("D", bools, "d");
		addVariable(d);
		addFunction(new Function(d, c, f));

		RandomVariable e = new RandomVariable("E", bools, "e");
		addVariable(e);
		addFunction(new Function(e, d, f));
    }
    
    public static void main(String[] argv)
    {
        MidtermModel2 model = new MidtermModel2();
        ModelVisualizer.draw(model);
        Distribution distribution = new PerfectDistribution(model);
        InductiveCausation search = new InductiveCausation(distribution);
        PartiallyDirectedGraph g = search.ic();
    }
    
}
