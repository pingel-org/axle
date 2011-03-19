package org.pingel.bayes

import org.pingel.util.UndirectedGraph

class EliminationTree
extends UndirectedGraph[EliminationTreeNode, EliminationTreeEdge]
{
	
  var node2phi = Map[EliminationTreeNode, Factor]()
	
  // public EliminationTree()	{ }

  def gatherVars(stop: EliminationTreeNode, node: EliminationTreeNode, result: Set[RandomVariable])
  {
    result.addAll(node2phi(node).getVariables);
    for( n <- getNeighbors(node)) {
      if( ! n.equals(stop) ) {
	gatherVars(node, n, result)
      }
    }
  }
	
  def cluster(i: EliminationTreeNode): Set[RandomVariable] = {
    var result = Set[RandomVariable]()
		
    for( j <- getNeighbors(i)) {
      result.addAll(separate(i, j))
    }
		
    result.addAll(node2phi(i).getVariables)
		
    result
  }

  // Set<RandomVariable>
	
  def separate(i: EliminationTreeNode, j: EliminationTreeNode): Set[RandomVariable] =
    {
      var iSide = Set[RandomVariable]()
      gatherVars(j, i, iSide)
		
      var jSide = Set[RandomVariable]()
      gatherVars(i, j, jSide)
		
      var result = Set[RandomVariable]()
      for( iv <- iSide) {
	if( jSide.contains(iv) ) {
	  result.add(iv)
	}
      }
		
      result
    }
	
  def constructEdge(v1: EliminationTreeNode, v2: EliminationTreeNode): EliminationTreeEdge = new EliminationTreeEdge(v1, v2)

  def delete(node: EliminationTreeNode): Unit = {
    super.delete(node)
    node2phi.remove(node)
  }
	
  def getAllVariables(): Set[RandomVariable] = {
    var result = Set[RandomVariable]()
    for( node <- node2phi.keySet ) {
      result.addAll(node2phi(node).getVariables());
    }
    result
  }
	
  def addFactor(node: EliminationTreeNode, f: Factor): Unit = {
    if( node2phi.contains(node) ) {
      node2phi += node -> existing.multiply(f)
    }
    else {
      node2phi += node -> f
    }
  }

  def getFactor(node: EliminationTreeNode): Factor = node2phi(node)
	
  def setFactor(node: EliminationTreeNode, f: Factor): Unit = node2phi += node -> f
	
  // UndirectedGraph[EliminationTreeNode, EliminationTreeEdge]
  def copyTo(other: EliminationTree): Unit = {
		
    for( node <- getVertices ) {
      other.addVertex(node)
    }
    
    for( edge <- getEdges ) {
      other.addEdge(edge)
    }
    
    for( node <- node2phi.keySet ) {
      other.setFactor(node, node2phi(node))
    }
      
  }
	
}
