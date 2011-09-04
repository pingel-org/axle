
package org.pingel.causality.examples;

import org.pingel.bayes.Factor
import org.pingel.bayes.InductiveCausation
import org.pingel.bayes.PartiallyDirectedGraph
import org.pingel.bayes.RandomVariable

object hw4 {
    
    def main(args: Array[String]) {
      
    	if( args.length != 4 ) {
    		System.err.println("Usage: java hw4 <k> <n> <p> {ic | icstar}")
    		exit(1)
    	}

    	val k = Integer.parseInt(args(0))
    	val n = Integer.parseInt(args(1))
    	val p = java.lang.Double.parseDouble(args(2))
    	val ic = args(3).equals("ic")
    	
    	
        val m = new Homework4Model(k, p)
        
        val table = m.sampleDistribution(n)

        val x0 = m.getVariable("X0")
        val x5 = m.getVariable("X5")
        val S = table.separate(x0, x5)
        exit(1)
        
		val search = new InductiveCausation(table)
		if( ic ) {
			val g = search.ic()
			println(g)
		}
		else {
			val g = search.icstar()
			println(g)
		}
    }
}
