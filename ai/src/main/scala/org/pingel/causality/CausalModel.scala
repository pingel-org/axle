
package org.pingel.causality

import org.pingel.bayes._
import org.pingel.ptype._

import org.pingel.util.Collector
import org.pingel.util.CrossProduct
import org.pingel.util.PowerSet


class CausalModel(name: String) extends org.pingel.bayes.Model(name) {

    var variable2function = Map[RandomVariable, PFunction]()
	
	def sampleDistribution(numSamples: Int) = {
        println("creating probabilitytable of " + getObservableRandomVariables().size() + " variables")
		var result = new Factor(getObservableRandomVariables())
		for( j <- 0 to (numSamples - 1) ) {
			val sample = getSample()
			val previous = result.read(sample)
			result.write(sample, previous + 1)
		}
		result
	}

	def addFunction(f: PFunction) = {
	    variable2function.put(f.rv, f)
	    for( i <- f.inputs) {
	    	connect(i, f.rv)
	    }
	}
	
	def getFunction(v: RandomVariable) = variable2function.get(v)

	def getSample(): Case = {
		var all = new Case()
		for( rv <- getRandomVariables() ) {
			getFunction(rv).execute(this, all)
		}
		all.projectToVars(getObservableRandomVariables())
	}

    def isSemiMarkovian() = getGraph().isAcyclic()

    def isMarkovian(): Boolean = {
        // page 69.  I'm not confident I have this right.
        for( rv <- getRandomVariables() ) {
            if( (! rv.observable) && (getGraph().getSuccessors(rv).size() > 1) ) {
                return false
            }
        }
        true
    }

    def identifies(p: Probability): Boolean = {
        if( isMarkovian() ) {
            
            // page 78 Theorem 3.2.5

            for( variable <- p.getActions() ) {
                val rv = variable.getRandomVariable()
                if( ! rv.observable ) {
                    return false
                }
                for( parent <- getGraph().getPredecessors(rv) ) {
                    if( ! parent.observable ) {
                        return false
                    }
                }
            }
            
            for( variable <- p.getQuestion() ) {
                if( ! variable.getRandomVariable().observable ) {
                    return false
                }
            }
            
            return true
        }
        else if ( isSemiMarkovian() ) {
            return hasDoor(p)
        }
        else {
            // TODO chapter 7 can help??
            throw new UnsupportedOperationException()
        }
    }

    def rvGetter = new Collector[Variable, RandomVariable]() {
    	override def function(variable: Variable) = variable.getRandomVariable()
    }

    
    def hasDoor(p: Probability): Boolean = {
        var V = Set[RandomVariable]()
        var questionRVs = rvGetter.execute(p.getQuestion())
        var actionRVs = rvGetter.execute(p.getActions())
        for( rv <- getRandomVariables() ) {
            if( rv.observable && ! questionRVs.contains(rv) && ! actionRVs.contains(rv) ) {
                V.add(rv)
            }
        }

        // a very naive search strategy
        
        for( pair <- new CrossProduct[RandomVariable](actionRVs, questionRVs) ) {
            for( Z <- new PowerSet[RandomVariable](V) ) {
                if( satisfiesBackdoorCriterion(pair.get(0), pair.get(1), Z) ||
                        satisfiesFrontdoorCriterion(pair.get(0), pair.get(1), Z) ) {
                    return true
                }
            }
        }
        false
    }
    
    def allBackdoorsBlocked(XiSet: Set[RandomVariable], XjSet: Set[RandomVariable], Z: Set[RandomVariable]) = {
        var subModel = duplicate()
        subModel.getGraph().removeOutputs(XiSet)
        subModel.blocks(XiSet, XjSet, Z)
    }
    
	def satisfiesBackdoorCriterion(Xi: RandomVariable, Xj: RandomVariable, Z: Set[RandomVariable]): Boolean = {
	    // Definition 3.3.1; page 79
        
	    // i) no node in Z is a descendant of Xi
        
        var descendants = Set[RandomVariable]()
        getGraph().collectDescendants(Xi, descendants)
        for( z <- Z ) {
            if( descendants.contains(z) ) {
                return false
            }
        }

        // and
        
	    // ii) Z blocks every path between Xi and Xj that contains an arrow into Xi

        var XiSet = Set[RandomVariable](Xi)
        var XjSet = Set[RandomVariable](Xj)

        allBackdoorsBlocked(XiSet, XjSet, Z)
        
	}

    def pathsInterceptBefore(from: RandomVariable, interceptors: Set[RandomVariable], to: RandomVariable): Boolean = {
        if( from == to ) {
            return false
        }
        
        if( ! interceptors.contains(from) ) {
            for( rv <- getGraph().getSuccessors(from)) {
                if( ! pathsInterceptBefore(rv, interceptors, to) ) {
                    return false
                }
            }
        }
        
        true
    }
    
	def satisfiesFrontdoorCriterion(X: RandomVariable, Y: RandomVariable, Z: Set[RandomVariable]) = {
        // Definition 3.3.3 page 82
        
        // i) Z intercepts all directed paths from X to Y;
        // ii) there is no back-door path from X to Z; and
        // iii) all back-door paths from Z to Y are blocked by X

        val XSet = Set[RandomVariable](X)
        val YSet = Set[RandomVariable](Y)
        
        pathsInterceptBefore(X, Z, Y) &&
          allBackdoorsBlocked(XSet, Z, Set[RandomVariable]()) &&
          allBackdoorsBlocked(Z, YSet, XSet);
    }


	def duplicate() = {
		var answer = new CausalModel(name)
		for( v <- getRandomVariables() ) {
			answer.addVariable(v)
		}
		for( v <- getRandomVariables() ) {
            for( output <- getGraph().getSuccessors(v) ) {
				answer.connect(v, output)
			}
		}
		answer.variable2function = Map[RandomVariable, Function]()
        answer.variable2function.putAll(variable2function)
		answer
	}
	
	
		
}
