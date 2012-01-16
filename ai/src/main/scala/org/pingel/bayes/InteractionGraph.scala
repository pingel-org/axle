package org.pingel.bayes

import org.pingel.axle.graph.UndirectedGraph
import scala.collection._

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

  def eliminationSequence(π: List[RandomVariable]): List[InteractionGraph] = {

    var result = mutable.ListBuffer[InteractionGraph]()
    var G: InteractionGraph = this
    result += G
		
    π.map( rv => {
      var newG = new InteractionGraph()
      G.copyTo(newG)
      newG.eliminate(rv)
      result += newG
      G = newG
    })
    
    result.toList
  }

}
