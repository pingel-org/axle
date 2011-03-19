package org.pingel.bayes;

import org.pingel.util.UndirectedGraphEdge;

public class EliminationTreeEdge
extends UndirectedGraphEdge<EliminationTreeNode>
{

	public EliminationTreeEdge(EliminationTreeNode v1, EliminationTreeNode v2)
	{
		super(v1, v2);
	}
}
