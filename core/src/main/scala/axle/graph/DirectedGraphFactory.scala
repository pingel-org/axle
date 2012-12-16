package axle.graph

import collection._

trait DirectedGraph[VP, EP] {

  type G[VP, EP] <: DirectedGraph[VP, EP]
  type ES  
  
  def vertices(): Set[Vertex[VP]]
  def edges(): Set[Edge[ES, EP]]

  def size(): Int

  def source(edge: Edge[ES, EP]): Vertex[VP]
  def dest(edge: Edge[ES, EP]): Vertex[VP]
  
  def deleteVertex(v: Vertex[VP]): G[VP, EP]
  def findVertex(f: Vertex[VP] => Boolean): Option[Vertex[VP]]
  def findEdge(from: Vertex[VP], to: Vertex[VP]): Option[Edge[ES, EP]]
  def leaves(): Set[Vertex[VP]]
  def neighbors(v: Vertex[VP]): Set[Vertex[VP]]
  def precedes(v1: Vertex[VP], v2: Vertex[VP]): Boolean
  def predecessors(v: Vertex[VP]): Set[Vertex[VP]]
  def isLeaf(v: Vertex[VP]): Boolean
  def successors(v: Vertex[VP]): Set[Vertex[VP]]
  def outputEdgesOf(v: Vertex[VP]): Set[Edge[ES, EP]]
  def descendantsIntersectsSet(v: Vertex[VP], s: Set[Vertex[VP]]): Boolean

  def _descendants(v: Vertex[VP], result: mutable.Set[Vertex[VP]]): Unit = {
    // inefficient
    if (!result.contains(v)) {
      result += v
      successors(v).map(_descendants(_, result))
    }
  }

  def descendants(v: Vertex[VP]): Set[Vertex[VP]] = {
    val result = mutable.Set[Vertex[VP]]()
    _descendants(v, result)
    result.toSet
  }

  // inefficient
  def _ancestors(v: Vertex[VP], result: mutable.Set[Vertex[VP]]): Unit = {
    if (!result.contains(v)) {
      result += v
      predecessors(v).map(_ancestors(_, result))
    }
  }

  def ancestors(v: Vertex[VP]): Set[Vertex[VP]] = {
    val result = mutable.Set[Vertex[VP]]()
    _ancestors(v, result)
    result.toSet
  }

  def ancestors(vs: Set[Vertex[VP]]): Set[Vertex[VP]] = {
    val result = mutable.Set[Vertex[VP]]()
    vs.map(_ancestors(_, result))
    result.toSet
  }

  def isAcyclic(): Boolean

  def shortestPath(source: Vertex[VP], goal: Vertex[VP]): Option[List[Edge[ES, EP]]]
  // def moralGraph(): UndirectedGraph[_, _] = null // TODO !!!

  // def deleteEdge(e: Edge[VP, EP]): DirectedGraph[VP, EP]

  // def deleteVertex(v: Vertex[VP]): DirectedGraph[VP, EP]

  //  def removeInputs(vs: Set[V]): GenDirectedGraph[VP, EP]
  //  def removeOutputs(vs: Set[V]): GenDirectedGraph[VP, EP]

}
