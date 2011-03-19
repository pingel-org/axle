package org.pingel.bayes

import org.pingel.util.UndirectedGraphVertex

class EliminationTreeNode(label: String)
extends UndirectedGraphVertex[EliminationTreeEdge]
{
  def getLabel(): String = label

}
