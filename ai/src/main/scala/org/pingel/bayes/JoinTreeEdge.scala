package org.pingel.bayes

import org.pingel.util.UndirectedGraphEdge

class JoinTreeEdge(v1: JoinTreeNode, v2: JoinTreeNode)
extends UndirectedGraphEdge[JoinTreeNode]
{
	def getVertices() = (v1, v2)
}
