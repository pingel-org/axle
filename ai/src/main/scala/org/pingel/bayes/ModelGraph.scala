package org.pingel.bayes;

import java.util.HashMap;
import java.util.Map;

import org.pingel.util.DirectedGraph;

public class ModelGraph extends DirectedGraph<RandomVariable, ModelEdge>
{
	Map<String, RandomVariable> name2variable = new HashMap<String, RandomVariable>();
	
	public RandomVariable addVertex(RandomVariable var)
	{
		return super.addVertex(var);
	}
	
	public RandomVariable getVariable(String name)
	{
		return name2variable.get(name);
	}
}
