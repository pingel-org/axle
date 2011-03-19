package org.pingel.bayes;

import org.pingel.util.UndirectedGraphVertex;

public class JoinTreeNode
implements UndirectedGraphVertex<JoinTreeEdge>
{
	private String name;
	
	public JoinTreeNode(String name)
	{
		this.name = name;
	}
	
	public String getLabel()
	{
		// Note: we should probably return cluster contents instead
		return name;
	}
}

