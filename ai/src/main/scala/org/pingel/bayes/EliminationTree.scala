package org.pingel.bayes

import scala.collection._
import org.pingel.axle.graph.UndirectedGraph
import org.pingel.axle.graph.UndirectedGraphVertex
import org.pingel.axle.graph.UndirectedGraphEdge

class EliminationTreeEdge(v1: EliminationTreeNode, v2: EliminationTreeNode)
extends UndirectedGraphEdge[EliminationTreeNode]
{
	def getVertices() = (v1, v2)
}

class EliminationTreeNode(label: String)
extends UndirectedGraphVertex[EliminationTreeEdge]
{
  def getLabel(): String = label

}


class EliminationTree
extends UndirectedGraph[EliminationTreeNode, EliminationTreeEdge]
{
	
  var node2phi = mutable.Map[EliminationTreeNode, Factor]()
	
  // public EliminationTree()	{ }

  def gatherVars(stop: EliminationTreeNode, node: EliminationTreeNode, result: mutable.Set[RandomVariable])
  {
    result ++= node2phi(node).getVariables
    for( n <- getNeighbors(node)) {
      if( ! n.equals(stop) ) {
    	  gatherVars(node, n, result)
      }
    }
  }
	
  def cluster(i: EliminationTreeNode): Set[RandomVariable] = {
    var result = mutable.Set[RandomVariable]()
    for( j <- getNeighbors(i)) {
      result ++= separate(i, j)
    }
    result ++= node2phi(i).getVariables
    result
  }

  // Set<RandomVariable>
	
  def separate(i: EliminationTreeNode, j: EliminationTreeNode): Set[RandomVariable] =
    {
      var iSide = mutable.Set[RandomVariable]()
      gatherVars(j, i, iSide)
		
      var jSide = mutable.Set[RandomVariable]()
      gatherVars(i, j, jSide)

      iSide.filter( iv => jSide.contains(iv) ) // aka "intersection"
    }
	
  def constructEdge(v1: EliminationTreeNode, v2: EliminationTreeNode): EliminationTreeEdge = new EliminationTreeEdge(v1, v2)

  def delete(node: EliminationTreeNode): Unit = {
    super.delete(node)
    node2phi -= node
  }
	
  def getAllVariables(): Set[RandomVariable] = {
    var result = Set[RandomVariable]()
    for( node <- node2phi.keySet ) {
      result ++= node2phi(node).getVariables()
    }
    result
  }
	
  def addFactor(node: EliminationTreeNode, f: Factor): Unit = {
    node2phi += node -> ( node2phi.contains(node) match {
      case true  => existing.multiply(f)
      case false => f
    })
  }

  def getFactor(node: EliminationTreeNode): Factor = node2phi(node)
	
  def setFactor(node: EliminationTreeNode, f: Factor): Unit = node2phi += node -> f
	
  // UndirectedGraph[EliminationTreeNode, EliminationTreeEdge]
  def copyTo(other: EliminationTree): Unit = {
    getVertices.map( node => other.addVertex(node) )
    getEdges.map( edge => other.addEdge(edge) )
    node2phi.keySet.map( node => other.setFactor(node, node2phi(node)) )
  }
	
}
