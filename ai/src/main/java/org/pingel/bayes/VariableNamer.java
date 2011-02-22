/*
 * Created on Jun 2, 2005
 *
 */
package org.pingel.bayes;

import java.util.HashMap;
import java.util.Map;


public class VariableNamer {

    private Map<RandomVariable, Integer> counts;

    public VariableNamer()
    {
        counts =  new HashMap<RandomVariable, Integer>();
    }
    
    public int increment(RandomVariable rv)
    {
        int c;
        Integer count = counts.get(rv);
        if( count == null ) {
            c = 0;
        }
        else {
            c = count.intValue();
        }
        counts.put(rv, new Integer(c+1));
        return c;

    }
    
    public VariableNamer duplicate()
    {
        VariableNamer duplicate = new VariableNamer();

        duplicate.counts.putAll(counts);
        
        return duplicate;
    }
}
