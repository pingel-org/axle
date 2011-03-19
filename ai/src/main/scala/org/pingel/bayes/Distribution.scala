package org.pingel.bayes;


import java.util.ArrayList;
import java.util.List;

abstract public class Distribution
{
    protected List<RandomVariable> varList = new ArrayList<RandomVariable>();

    public Distribution(RandomVariable... rvs)
    {
    		for(RandomVariable rv : rvs) {
    			varList.add(rv);
    		}
    }
    
    public Distribution(List<RandomVariable> variables)
    {
    		varList.addAll(variables);
    }
    
    public List<RandomVariable> getVariables()
    {
        return varList;
    }

}
