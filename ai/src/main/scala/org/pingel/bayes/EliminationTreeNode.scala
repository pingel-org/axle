package org.pingel.bayes;

import org.pingel.util.UndirectedGraphVertex;

public class EliminationTreeNode
implements UndirectedGraphVertex<EliminationTreeEdge>
{
	private String label;
	
	public EliminationTreeNode(String label)
	{
		this.label = label;
	}
	
	public String getLabel()
	{
		return label;
	}
}
