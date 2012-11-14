package axle.graph

import collection._

trait GenDirectedGraphFactory extends GraphFactory {

  def apply[VP, EP](): GenDirectedGraph[VP, EP]

  def apply[VP, EP](vps: Seq[VP], ef: Seq[DirectedGraphVertex[VP]] => Seq[(DirectedGraphVertex[VP], DirectedGraphVertex[VP], EP)]): GenDirectedGraph[VP, EP]

}

trait DirectedGraphVertex[VP] extends GraphVertex[VP]

trait DirectedGraphEdge[VP, EP] extends GraphEdge[VP, EP] {
  def source(): DirectedGraphVertex[VP]
  def dest(): DirectedGraphVertex[VP]
}

trait GenDirectedGraph[VP, EP] extends GenGraph[VP, EP] {

//  type V <: DirectedGraphVertex[VP]
//  type E <: DirectedGraphEdge[VP, EP]

  def findEdge(from: DirectedGraphVertex[VP], to: DirectedGraphVertex[VP]): Option[DirectedGraphEdge[VP, EP]]
  def leaves(): Set[DirectedGraphVertex[VP]]
  def neighbors(v: DirectedGraphVertex[VP]): Set[DirectedGraphVertex[VP]]
  def precedes(v1: DirectedGraphVertex[VP], v2: DirectedGraphVertex[VP]): Boolean
  def predecessors(v: DirectedGraphVertex[VP]): Set[DirectedGraphVertex[VP]]
  def isLeaf(v: DirectedGraphVertex[VP]): Boolean
  def successors(v: DirectedGraphVertex[VP]): Set[DirectedGraphVertex[VP]]
  def outputEdgesOf(v: DirectedGraphVertex[VP]): Set[DirectedGraphEdge[VP, EP]]
  def descendantsIntersectsSet(v: DirectedGraphVertex[VP], s: Set[DirectedGraphVertex[VP]]): Boolean

  def _descendants(v: DirectedGraphVertex[VP], result: mutable.Set[DirectedGraphVertex[VP]]): Unit = {
    // inefficient
    if (!result.contains(v)) {
      result += v
      successors(v).map(_descendants(_, result))
    }
  }

  def descendants(v: DirectedGraphVertex[VP]): Set[DirectedGraphVertex[VP]] = {
    val result = mutable.Set[DirectedGraphVertex[VP]]()
    _descendants(v, result)
    result.toSet
  }

  // inefficient
  def _ancestors(v: DirectedGraphVertex[VP], result: mutable.Set[DirectedGraphVertex[VP]]): Unit = {
    if (!result.contains(v)) {
      result += v
      predecessors(v).map(_ancestors(_, result))
    }
  }

  def ancestors(v: DirectedGraphVertex[VP]): Set[DirectedGraphVertex[VP]] = {
    val result = mutable.Set[DirectedGraphVertex[VP]]()
    _ancestors(v, result)
    result.toSet
  }

  def ancestors(vs: Set[DirectedGraphVertex[VP]]): Set[DirectedGraphVertex[VP]] = {
    val result = mutable.Set[DirectedGraphVertex[VP]]()
    vs.map(_ancestors(_, result))
    result.toSet
  }

  def isAcyclic(): Boolean
  
  def shortestPath(source: DirectedGraphVertex[VP], goal: DirectedGraphVertex[VP]): Option[List[DirectedGraphEdge[VP, EP]]]
  // def moralGraph(): UndirectedGraph[_, _] = null // TODO !!!

  def deleteEdge(e: DirectedGraphEdge[VP, EP]): GenDirectedGraph[VP, EP]
  
  def deleteVertex(v: DirectedGraphVertex[VP]): GenDirectedGraph[VP, EP]
  
//  def removeInputs(vs: Set[V]): GenDirectedGraph[VP, EP]
//  def removeOutputs(vs: Set[V]): GenDirectedGraph[VP, EP]

}
