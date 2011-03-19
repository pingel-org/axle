package org.pingel.bayes;

import org.pingel.util.DirectedGraphEdge;

public class ModelEdge extends DirectedGraphEdge<RandomVariable>
{

    public ModelEdge(RandomVariable v1, RandomVariable v2)
    {
        super(v1, v2);
    }
}
