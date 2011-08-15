
package org.pingel.bayes;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.pingel.util.Collector;
import org.pingel.util.CrossProduct;
import org.pingel.util.PowerSet;

public class CausalModel extends Model
{
    public CausalModel() { super(); }

    public CausalModel(String name) { super(name); }
    
    Map<RandomVariable, Function> variable2function = new HashMap<RandomVariable, Function>();
	
	public Factor sampleDistribution(int numSamples)
	{
        System.out.println("creating probabilitytable of " + getObservableRandomVariables().size() + " variables");
		Factor result = new Factor(getObservableRandomVariables());
		
		for(int j=0; j < numSamples; j++) {
			Case sample = getSample();
			double previous = result.read(sample);
			result.write(sample, previous + 1);
		}
		
		return result;
	}

	protected void addFunction(Function f)
	{
	    variable2function.put(f.rv, f);

	    for(int i=0; i < f.inputs.size(); i++) 
	    {
	    	connect(f.inputs.get(i), f.rv);
	    }
	}
	
	protected Function getFunction(RandomVariable v)
	{
	    return variable2function.get(v);
	}
	
	public Case getSample()
	{
		Case all = new Case();
		for( RandomVariable rv : getRandomVariables() ) {
			getFunction(rv).execute(this, all);
		}
		return all.projectToVars(getObservableRandomVariables());
	}

    public boolean isSemiMarkovian()
    {
        return getGraph().isAcyclic();
    }

    public boolean isMarkovian()
    {
        // page 69.  I'm not confident I have this right.
        
        for( RandomVariable var : getRandomVariables() ) {
            if( (! var.observable) &&
                    (getGraph().getSuccessors(var).size() > 1) ) {
                return false;
            }
        }
        return true;
    }

    public boolean identifies(Probability p)
    {
        if( isMarkovian() ) {
            
            // page 78 Theorem 3.2.5

            for( RandomVariable var : p.getActions() ) {
                RandomVariable rv = var.getRandomVariable();
                if( ! rv.observable ) {
                    return false;
                }
                for( RandomVariable parent : getGraph().getPredecessors(rv) ) {
                    if( ! parent.observable ) {
                        return false;
                    }
                }
            }
            
            for( RandomVariable var : p.getQuestion() ) {
                if( ! var.getRandomVariable().observable ) {
                    return false;
                }
            }
            
            return true;
        }
        else if ( isSemiMarkovian() ) {
            return hasDoor(p);
        }
        else {
            // TODO chapter 7 can help??
            throw new UnsupportedOperationException();
        }
    }

    protected Collector<Variable, RandomVariable> rvGetter = new Collector<Variable, RandomVariable>() {
        public RandomVariable function(Variable var) {
            return var.getRandomVariable();
        }
    };

    
    public boolean hasDoor(Probability p)
    {
        Set<RandomVariable> V = new HashSet<RandomVariable>();
        Set<RandomVariable> questionRVs = rvGetter.execute(p.getQuestion());
        Set<RandomVariable> actionRVs = rvGetter.execute(p.getActions());
        for( RandomVariable rv : getRandomVariables() ) {
            if( rv.observable && ! questionRVs.contains(rv) && ! actionRVs.contains(rv) ) {
                V.add(rv);
            }
        }

        // a very naive search strategy
        
        for( List<RandomVariable> pair : new CrossProduct<RandomVariable>(actionRVs, questionRVs) ) {
            for( Set<RandomVariable> Z : new PowerSet<RandomVariable>(V) ) {
                if( satisfiesBackdoorCriterion(pair.get(0), pair.get(1), Z) ||
                        satisfiesFrontdoorCriterion(pair.get(0), pair.get(1), Z) ) {
                    return true;
                }
            }
        }
        return false;
    }
    
    private boolean allBackdoorsBlocked(Set<RandomVariable> XiSet, Set<RandomVariable> XjSet, Set<RandomVariable> Z)
    {
        CausalModel subModel = duplicate();
        subModel.getGraph().removeOutputs(XiSet);

        return subModel.blocks(XiSet, XjSet, Z);
    }
    
	public boolean satisfiesBackdoorCriterion(RandomVariable Xi, RandomVariable Xj, Set<RandomVariable> Z)
	{
	    // Definition 3.3.1; page 79
        
	    // i) no node in Z is a descendant of Xi
        
        Set<RandomVariable> descendants = new HashSet<RandomVariable>();
        getGraph().collectDescendants(Xi, descendants);
        for( RandomVariable z : Z ) {
            if( descendants.contains(z) ) {
                return false;
            }
        }

        // and
        
	    // ii) Z blocks every path between Xi and Xj that contains an arrow into Xi

        Set<RandomVariable> XiSet = new HashSet<RandomVariable>();
        XiSet.add(Xi);
        Set<RandomVariable> XjSet = new HashSet<RandomVariable>();
        XjSet.add(Xj);

        return allBackdoorsBlocked(XiSet, XjSet, Z);
        
	}

    private boolean pathsInterceptBefore(RandomVariable from, Set<RandomVariable> interceptors, RandomVariable to)
    {
        if( from == to ) {
            return false;
        }
        
        if( ! interceptors.contains(from) ) {
            
            for( RandomVariable rv : getGraph().getSuccessors(from)) {
                if( ! pathsInterceptBefore(rv, interceptors, to) ) {
                    return false;
                }
            }
        }
        
        return true;
    }
    
	public boolean satisfiesFrontdoorCriterion(RandomVariable X, RandomVariable Y, Set<RandomVariable> Z) 
	{
        // Definition 3.3.3 page 82
        
        // i) Z intercepts all directed paths from X to Y;
        // ii) there is no back-door path from X to Z; and
        // iii) all back-door paths from Z to Y are blocked by X

        Set<RandomVariable> XSet = new HashSet<RandomVariable>();
        XSet.add(X);
        Set<RandomVariable> YSet = new HashSet<RandomVariable>();
        YSet.add(Y);
        
        return
        pathsInterceptBefore(X, Z, Y) &&
        allBackdoorsBlocked(XSet, Z, new HashSet<RandomVariable>()) &&
        allBackdoorsBlocked(Z, YSet, XSet);
    }


	public CausalModel duplicate()
	{
		CausalModel answer = new CausalModel();
		for( RandomVariable var : getRandomVariables() ) {
			answer.addVariable(var);
		}
		
		for( RandomVariable var : getRandomVariables() ) {
			Set<RandomVariable> outputs = getGraph().getSuccessors(var);
            for( RandomVariable out : outputs ) {
				answer.connect(var, out);
			}
		}
		
		answer.variable2function = new HashMap<RandomVariable, Function>();
        answer.variable2function.putAll(variable2function);

		return answer;
	}
	
	
		
}
