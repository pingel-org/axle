
package org.pingel.causality;

import java.util.Set;

import org.pingel.bayes.Distribution;
import org.pingel.bayes.RandomVariable;


public class PerfectDistribution extends Distribution
{
    private CausalModel model;
    
    public PerfectDistribution(CausalModel model)
    {
        super(model.getRandomVariables());
        this.model = model;
    }

    
    public Set<RandomVariable> separate(RandomVariable a, RandomVariable b)
    {
        // TODO
        return null;
    }
}
