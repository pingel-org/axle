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

  type V <: DirectedGraphVertex[VP]
  type E <: DirectedGraphEdge[VP, EP]

  def findEdge(from: V, to: V): Option[E]
  def leaves(): Set[V]
  def neighbors(v: V): Set[V]
  def precedes(v1: V, v2: V): Boolean
  def predecessors(v: V): Set[V]
  def isLeaf(v: V): Boolean
  def successors(v: V): Set[V]
  def outputEdgesOf(v: V): Set[E]
  def descendantsIntersectsSet(v: V, s: Set[V]): Boolean

  def _descendants(v: V, result: mutable.Set[V]): Unit = {
    // inefficient
    if (!result.contains(v)) {
      result += v
      successors(v).map(_descendants(_, result))
    }
  }

  def descendants(v: V): Set[V] = {
    val result = mutable.Set[V]()
    _descendants(v, result)
    result.toSet
  }

  // inefficient
  def _ancestors(v: V, result: mutable.Set[V]): Unit = {
    if (!result.contains(v)) {
      result += v
      predecessors(v).map(_ancestors(_, result))
    }
  }

  def ancestors(v: V): Set[V] = {
    val result = mutable.Set[V]()
    _ancestors(v, result)
    result.toSet
  }

  def ancestors(vs: Set[V]): Set[V] = {
    val result = mutable.Set[V]()
    vs.map(_ancestors(_, result))
    result.toSet
  }

  def isAcyclic(): Boolean
  def shortestPath(source: V, goal: V): Option[List[E]]
  // def moralGraph(): UndirectedGraph[_, _] = null // TODO !!!

  def deleteEdge(e: E): GenDirectedGraph[VP, EP]
  def deleteVertex(v: V): GenDirectedGraph[VP, EP]
  def removeInputs(vs: Set[V]): GenDirectedGraph[VP, EP]
  def removeOutputs(vs: Set[V]): GenDirectedGraph[VP, EP]

}
