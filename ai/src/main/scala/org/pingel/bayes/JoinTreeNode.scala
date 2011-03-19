package org.pingel.bayes;

import org.pingel.util.UndirectedGraphVertex;

class JoinTreeNode(name: String)
extends UndirectedGraphVertex[JoinTreeEdge]
{
  // Note: we should probably return cluster contents instead
 def getLabel(): String = name

}

