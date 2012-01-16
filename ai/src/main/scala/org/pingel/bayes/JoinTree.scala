package org.pingel.bayes;

import scala.collection._
import org.pingel.axle.graph.DirectedGraph
import org.pingel.axle.graph.UndirectedGraph
import org.pingel.axle.graph.UndirectedGraphEdge
import org.pingel.axle.graph.UndirectedGraphVertex

class JoinTreeNode(name: String)
extends UndirectedGraphVertex[JoinTreeEdge]
{
	// Note: we should probably return cluster contents instead
	def getLabel(): String = name
}

class JoinTreeEdge(v1: JoinTreeNode, v2: JoinTreeNode)
extends UndirectedGraphEdge[JoinTreeNode]
{
	def getVertices() = (v1, v2)
}

object JoinTree {

  // TODO:
  def fromEliminationOrder(G: DirectedGraph[_, _], pi: List[RandomVariable]): JoinTree = {
    // returns a jointree for DAG G with width equal to width(pi, G)
    var T = new JoinTree()
    val Gm = G.moralGraph() // UndirectedGraph
    var clusterSequence: List[Set[RandomVariable]] = null // Gm.induceClusterSequence(pi);
    T
  }

}

class JoinTree extends UndirectedGraph[JoinTreeNode, JoinTreeEdge]
{
	
  var node2cluster = mutable.Map[JoinTreeNode, Set[RandomVariable]]().withDefaultValue(Set[RandomVariable]())
  
  def copyTo(other: UndirectedGraph[JoinTreeNode, JoinTreeEdge]): Unit = 
  {
    // asdf();
  }

  def setCluster(n: JoinTreeNode, cluster: Set[RandomVariable]): Unit = node2cluster += n -> cluster

  def addToCluster(n: JoinTreeNode, v: RandomVariable): Unit = {
    node2cluster(n) += v
  }
	
  def constructEdge(n1: JoinTreeNode, n2: JoinTreeNode): JoinTreeEdge = new JoinTreeEdge(n1, n2)
  

  def separate(n1: JoinTreeNode, n2: JoinTreeNode): Set[RandomVariable] = {
    var result = Set[RandomVariable]()
    
    for( v <- node2cluster.get(n1)) {
      if( node2cluster(n2).contains(v) ) {
    	  result += v
      }
    }
    result
  }


  def toEliminationOrder(r: JoinTreeNode): List[RandomVariable] =  {
    var result = new mutable.ListBuffer[RandomVariable]()
		
    var T = new JoinTree()
    copyTo(T) // not yet implemented
		
    while( T.getVertices().size > 1 ) {
      val i = T.firstLeafOtherThan(r)
      val j = null // TODO theNeighbor(); a JoinTreeNode
      for(v <- node2cluster.get(i)) {
    	  if( ! node2cluster(j).contains(v) ) {
    		  result += v
    	  }
      }
    }

    for( v <- node2cluster(r) )  {
      result += v
    }
		
    result.toList
  }
	
  def embeds(eTree: EliminationTree, embedding: Map[JoinTreeNode, EliminationTreeNode]): Boolean = {
    for(jtn <- getVertices() ) {
      val cluster = node2cluster(jtn)
      val etn = embedding(jtn)
      for( v <- eTree.getFactor(etn).getVariables ) {
    	  if( ! cluster.contains(v) ) {
    		  return false
    	  }
      }
    }
    
    true
  }
	
}
