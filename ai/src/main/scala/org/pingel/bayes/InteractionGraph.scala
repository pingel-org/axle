package org.pingel.bayes

import org.pingel.util.UndirectedGraph

class InteractionGraph extends UndirectedGraph[RandomVariable, VariableLink] {

  def constructEdge(v1: RandomVariable, v2: RandomVariable) =  new VariableLink(v1, v2)

  def copyTo(other: UndirectedGraph[RandomVariable, VariableLink]): Unit = {
    // should this be shallow or deep copies of the vertex/edge sets
		
    for( rv <- getVertices() ) {
      other.addVertex(rv)
    }
		
    for( vl <- getEdges() ) {
      other.addEdge(vl)
    }
  }

  def eliminationSequence(pi: List[RandomVariable]): List[InteractionGraph] = {

    var result = List[InteractionGraph]()
    var G: InteractionGraph = this
    result.add(G)
		
    for( rv <- pi ) {
      var newG = new InteractionGraph()
      G.copyTo(newG)
      newG.eliminate(rv)
      result.add(newG)
      G = newG
    }
    
    result
  }

}
