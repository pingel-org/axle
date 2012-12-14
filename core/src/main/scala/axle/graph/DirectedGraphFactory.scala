package axle.graph

import collection._

trait DirectedGraph[VP, EP] {
  
  type G[VP, EP] <: DirectedGraph[VP, EP]
  type V[VP] <: DirectedGraphVertex[VP]
  type E[VP, EP] <: DirectedGraphEdge[VP, EP]
  
  def vertices(): Set[V[VP]]
  def edges(): Set[E[VP, EP]]

  def size(): Int
  
  def deleteVertex(v: V[VP]): DirectedGraph[VP, EP]
  def findVertex(f: V[VP] => Boolean): Option[V[VP]]
  def findEdge(from: V[VP], to: V[VP]): Option[E[VP, EP]]
  def leaves(): Set[V[VP]]
  def neighbors(v: V[VP]): Set[V[VP]]
  def precedes(v1: V[VP], v2: V[VP]): Boolean
  def predecessors(v: V[VP]): Set[V[VP]]
  def isLeaf(v: V[VP]): Boolean
  def successors(v: V[VP]): Set[V[VP]]
  def outputEdgesOf(v: V[VP]): Set[E[VP, EP]]
  def descendantsIntersectsSet(v: V[VP], s: Set[V[VP]]): Boolean

  def _descendants(v: V[VP], result: mutable.Set[V[VP]]): Unit = {
    // inefficient
    if (!result.contains(v)) {
      result += v
      successors(v).map(_descendants(_, result))
    }
  }

  def descendants(v: V[VP]): Set[V[VP]] = {
    val result = mutable.Set[V[VP]]()
    _descendants(v, result)
    result.toSet
  }

  // inefficient
  def _ancestors(v: V[VP], result: mutable.Set[V[VP]]): Unit = {
    if (!result.contains(v)) {
      result += v
      predecessors(v).map(_ancestors(_, result))
    }
  }

  def ancestors(v: V[VP]): Set[V[VP]] = {
    val result = mutable.Set[V[VP]]()
    _ancestors(v, result)
    result.toSet
  }

  def ancestors(vs: Set[V[VP]]): Set[V[VP]] = {
    val result = mutable.Set[V[VP]]()
    vs.map(_ancestors(_, result))
    result.toSet
  }

  def isAcyclic(): Boolean

  def shortestPath(source: V[VP], goal: V[VP]): Option[List[E[VP, EP]]]
  // def moralGraph(): UndirectedGraph[_, _] = null // TODO !!!

  // def deleteEdge(e: DirectedGraphEdge[VP, EP]): DirectedGraph[VP, EP]

  // def deleteVertex(v: DirectedGraphVertex[VP]): DirectedGraph[VP, EP]

  //  def removeInputs(vs: Set[V]): GenDirectedGraph[VP, EP]
  //  def removeOutputs(vs: Set[V]): GenDirectedGraph[VP, EP]

}

trait DirectedGraphEdge[VP, EP] {

  type V[VP] <: DirectedGraphVertex[VP]
  
  def payload(): EP
  def source(): V[VP]
  def dest(): V[VP]
}

trait DirectedGraphVertex[VP] {
  def payload(): VP
}

trait DirectedGraphFactory {

  type G[VP, EP] <: DirectedGraph[VP, EP]
  type V[VP] <: DirectedGraphVertex[VP]

  def apply[VP, EP](vps: Seq[VP], ef: Seq[V[VP]] => Seq[(V[VP], V[VP], EP)]): G[VP, EP]

}
