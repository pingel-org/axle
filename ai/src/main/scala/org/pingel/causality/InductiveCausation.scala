
package org.pingel.bayes

class InductiveCausation(pHat: Distribution)
{
	val FALSE = new PBoolean(false)
	val TRUE = new PBoolean(true)

	var varList = List[RandomVariable]()
	varList.addAll(pHat.getVariables())

	def prepareGraph(): PartiallyDirectedGraph = {
	  
		val G = new PartiallyDirectedGraph(varList)

		// Set<RandomVariable>[][]
        var separators = Set[RandomVariable](varList.size, varList.size)
        
        for(i <- 0 to (varList.size - 2) ) {
            val a = varList(i)
            for( j <- (i+1) to (varList.size - 1) ) {
                val b = varList(j)

                val S = pHat.separate(a, b) // Set<RandomVariable>
           
                // Connect a and b with an undirected edge iff no set S_ab can be found
                if( S == null ) {
                    println("cannot separate " + a.name + " and " + b.name)
                    G.connect(a, b)
                }
                else {
                    println("separating ("+ a.name + ", " + b.name + ") with " + S)
                }

                separators[i][j] = S
                separators[j][i] = S

            }
        }

        	for( int i=0; i < (varList.size() - 1); i++) {
        		RandomVariable a = varList.get(i)
        		Vector<RandomVariable> aNeighbors = G.links(a, null, null, null)

        		for( int j=(i+1); j < varList.size(); j++) {
        			RandomVariable b = varList.get(j)
        			if( ! G.areAdjacent(a, b) ) {
        				Set S = separators[i][j]
        				if( S != null ) {
                    	
        					println("prepareGraph second loop")
                        
        					Vector<RandomVariable> bNeighbors = G.links(b, null, null, null);
        					Vector<RandomVariable> cList = intersection(aNeighbors, bNeighbors);
                    	
        					for( RandomVariable c : cList ) {
        						if( ! S.contains(c) ) {
        							G.connect(a, c);
        							G.orient(a, c);
        							G.connect(b, c);
        							G.orient(b, c);
        						}
        					}
        				}
        			}
        		}
        	}
        
        	G
	}

	def intersection(v1: List[RandomVariable], v2: List[RandomVariable]) = {
	  
		var result = ListBuffer[RandomVariable]()
		
		for( o1 <- v1 ) {
			var found = false
			var it2 = v2.iterator
			while( (! found ) && it2.hasNext ) {
				val o2 = it2.next()
				if( o1.equals(o2) ) {
					result.append(o1)
					found = true
				}
			}
		}
		result
	}
	
    def applyICR1(G: PartiallyDirectedGraph): Boolean = {
    	// R1: Orient b - c into b -> c whenever there is an arrow
        // a -> b such that a and c are nonadjacent

    	var applied = false
    	
        for( int i=0; i < (varList.size()-1); i++) {
            RandomVariable a = varList.get(i)
            Vector<RandomVariable> aOutputs = G.links(a, null, null, TRUE)
            for( int j=0; j < aOutputs.size(); j++) {
                RandomVariable b = aOutputs.get(j)
                Vector<RandomVariable> bNeighbors = G.links(b, null, null, FALSE)
                for( RandomVariable c : bNeighbors ) {
                	if( ! G.areAdjacent(a, c) ) {
                		G.orient(b, c)
                		applied = true
                	}
                }
            }
        }
        applied
    }

    def applyICR2(G: PartiallyDirectedGraph): Boolean = {
        // R2: Orient a - b into a -> b whenever there is chain a -> c -> b

    	var applied = false

    	for(int i=0; i < varList.size(); i++) {
    		RandomVariable a = varList.get(i)
    		
    		Vector<RandomVariable> aOutputs = G.links(a, null, null, TRUE);
    		for(int j=0; j < aOutputs.size(); j++) {
    			RandomVariable c = aOutputs.elementAt(j)
    			
    			Vector<RandomVariable> cOutputs = G.links(c, null, null, TRUE);
    			for(int m=0; m < cOutputs.size(); m++) {
    				RandomVariable b = cOutputs.elementAt(m)
    				if( G.undirectedAdjacent(a, b) ) {
    					G.orient(a, b)
    					applied = true
    				}
    			}
    		}
    	}
    	applied
    }

    def applyICR3(G: PartiallyDirectedGraph): Boolean = {
        // R3: Orient a - b into a -> b whenever there are two chains
        // a - c -> b and a - d -> b such that c and d are nonadjacent

    	var applied = false

    	for(int i=0; i < varList.size(); i++) {
    		RandomVariable a = varList.get(i);

    		Vector<RandomVariable> aNeighbors = G.links(a, null, null, FALSE);

        	for(int j=0; j < aNeighbors.size() - 1; j++) {

        		RandomVariable c = aNeighbors.elementAt(j);
        		
        		for(int m=j+1; m < aNeighbors.size(); m++) {

        			RandomVariable d = aNeighbors.elementAt(m);

        			if( ! G.undirectedAdjacent(c, d) ) {

        				Vector<RandomVariable> dOutputs = G.links(d, null, null, TRUE);
        				Vector<RandomVariable> cOutputs = G.links(c, null, null, TRUE);
        				
        				Vector<RandomVariable> dcOutputs = intersection(dOutputs, cOutputs);
        				
        				for( RandomVariable b : dcOutputs ) {

        					if( G.undirectedAdjacent(a, b) ) {
        						G.orient(a, b);
        						applied = true;
        					}
        					
        				}
        				
        			}
        			
        		}

        	}
    		
    	}
    	applied
    }

    def applyICR4(G: PartiallyDirectedGraph): Boolean = {
        // R4: Orient a - b into a -> b whenever there are two chains
        // a - c -> d and c -> d -> b such that c and b are nonadjacent
        // and a and d are adjacent.

    	var applied = false

    	for(int i=0; i < varList.size(); i++) {

    		RandomVariable a = varList.get(i);
    		Vector<RandomVariable> aNeighbors = G.links(a, null, null, null);
    		
    		for(int j=0; j < aNeighbors.size(); j++) {

    			RandomVariable c = aNeighbors.get(j);
    			Vector<RandomVariable> cOutputs = G.links(c, null, null, TRUE);
    			
    			for(int m=0; m < cOutputs.size(); m++) {
    				
    				RandomVariable d = cOutputs.elementAt(m);
    				
    				if( ! a.equals(d) ) {

    					Vector<RandomVariable> dOutputs = G.links(d, null, null, TRUE);
    					
    					if( G.areAdjacent(a, d) ) {
    						
    						Vector<RandomVariable> adOutputs = intersection(aNeighbors, dOutputs);
    						
    						for(int n=0; n < adOutputs.size(); n++) {
    							
    							RandomVariable b = adOutputs.get(n);
    							
    							if( ( ! b.equals(c) ) &&
    									(! G.areAdjacent(c, b)) ) {
    								G.orient(a, b);
    								applied = true;
    							}
    						}
    					}
    				}
    			}
    		}
    	}
    	applied
    }

    def applyICStarR1(G: PartiallyDirectedGraph): Boolean = {
    	/*
    	 * R1: For each pair of nonadjacent nodes a and b with a common neighbor c,
    	 * if the link between a and c has an arrowhead into c and if the link between
    	 * c and b has no arrowhead into c, then add an arrowhead on the link between
	 	 * c and b pointing at b and mark that link to obtain c -*-> b
	 	 */

    	var applied = false

    	for(int i=0; i < varList.size; i++) {

    		RandomVariable a = varList.get(i);

    		Vector<RandomVariable> cList = G.links(a, null, FALSE, TRUE);
    		
    		for(int j=0; j < cList.size(); j++) {
    			
    			RandomVariable c = cList.elementAt(j);
    			
    			Vector<RandomVariable> bList = G.links(c, FALSE, FALSE, null);
    			
    			for(int m=0; m < bList.size(); m++) {
    				
    				RandomVariable b = bList.elementAt(m);
    				
    				if( ! G.areAdjacent(a, b) ) {
    					G.orient(c, b);
    					G.mark(c, b);
    					applied = true;
    				}
    				
    			}
    		}
    	}
    	applied
    }

    def applyICStarR2(G: PartiallyDirectedGraph): Boolean = {
    	/* R2: If a and b are adjacent and there is a directed path (composed strictly
   		 * of marked links) from a to b (as in Figure 2.2), then add an arrowhead
   		 * pointing toward b on the link between a and b.
    	*/

    	var applied = false

    	for(int i=0; i < varList.size(); i++) {

    		RandomVariable a = varList.get(i);

    		Vector<RandomVariable> bList = G.links(a, null, null, null);

    		for(int j=0; j < bList.size(); j++) {
    			RandomVariable b = bList.get(j);
    			if( G.markedPathExists(a, b) ) {
    				G.orient(a, b);
    			}
    		}
    	}
    	applied
    }
	
    def ic(): PartiallyDirectedGraph = {
        // This code is based on the pseudocode in Pearl's "Causation" page 50
        
        // TODO assert: "pHat" is stable
         
        var G = prepareGraph()
        
        var proceed = true
        while( proceed ) {
            println("proceeding")
            proceed = applyICR1(G)
            proceed |= applyICR2(G)
            proceed |= applyICR3(G)
            proceed |= applyICR4(G)
        }
        G
    }
	
    def icstar(): PartiallyDirectedGraph = {
    	// This is from page 52 - 53

    	var G = prepareGraph()

    	/* In the partially directed graph that results, add (recursively) as many
    	 * arrowheads as possible, and mark as many edges as possible,
    	 * according to the following two rules:
    	 */

    	var proceed = true
    	while( proceed ) {
    		proceed = applyICStarR1(G)
    		proceed |= applyICStarR2(G)
    	}
    	G
    }
	
}
