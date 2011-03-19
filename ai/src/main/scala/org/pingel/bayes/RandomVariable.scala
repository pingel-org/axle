package org.pingel.bayes;

import org.pingel.util.DirectedGraphVertex;
import org.pingel.util.UndirectedGraphVertex;

public class RandomVariable
implements DirectedGraphVertex<ModelEdge>, UndirectedGraphVertex<VariableLink>, Comparable<RandomVariable>
{
	public String name;
	private String lcName;
	private Domain domain;

	public boolean observable = true;
	
	public RandomVariable(String name, Domain domain)
	{
		this.name = name;
		this.domain = domain;
	}

    public RandomVariable(String name, Domain domain, String lcName)
    {
        this.name = name;
        this.domain = domain;
        this.lcName = lcName;
    }
    
	public RandomVariable(String name, String lcName)
	{
		this.name = name;
		this.lcName = lcName;
	}

    public RandomVariable(String name, Domain domain, String lcName, boolean observable)
    {
        this.name = name;
        this.domain = domain;
        this.lcName = lcName;
        this.observable = observable;
    }

	public RandomVariable(String name, String lcName, boolean observable)
	{
		this.name = name;
		this.lcName = lcName;
		this.observable = observable;
	}
	
	public Domain getDomain()
	{
		return domain;
	}
    
    public int compareTo(RandomVariable other)
    {
        return name.compareTo(other.name);
    }

    public String toString()
    {
    		return name;
    }
    
    public String getLabel()
    {
    		return name;
    }
}
