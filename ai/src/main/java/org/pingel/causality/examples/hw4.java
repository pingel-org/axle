
package org.pingel.causality.examples;

import java.util.Set;

import org.pingel.bayes.Factor;
import org.pingel.bayes.InductiveCausation;
import org.pingel.bayes.PartiallyDirectedGraph;
import org.pingel.bayes.RandomVariable;

public class hw4
{
    
    public static void main(String[] argv)
    {
    	if( argv.length != 4 ) {
    		System.err.println("Usage: java hw4 <k> <n> <p> {ic | icstar}");
    		System.exit(1);
    	}

    	int k = Integer.parseInt(argv[0]);
    	int n = Integer.parseInt(argv[1]);
    	double p = Double.parseDouble(argv[2]);
    	boolean ic = argv[3].equals("ic");
    	
        Homework4Model m = new Homework4Model(k, p);
        
        Factor table = m.sampleDistribution(n);

        RandomVariable x0 = m.getVariable("X0");
        RandomVariable x5 = m.getVariable("X5");
        Set<RandomVariable> S = table.separate(x0, x5);
        System.exit(1);
        
		InductiveCausation search = new InductiveCausation(table);
		
		if( ic ) {
			PartiallyDirectedGraph g = search.ic();
			System.out.println(g);
		}
		else {
			PartiallyDirectedGraph g = search.icstar();
			System.out.println(g);
		}
    }
}
