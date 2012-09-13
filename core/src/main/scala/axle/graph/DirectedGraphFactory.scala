package axle.graph

trait DirectedGraphFactory extends GraphFactory {

  import collection._

  type G[VP, EP] <: DirectedGraph[VP, EP]

  trait DirectedGraph[VP, EP] extends Graph[VP, EP] {

    type V <: DirectedGraphVertex[VP]
    type E <: DirectedGraphEdge[EP]

    trait DirectedGraphVertex[P] extends GraphVertex[P]

    trait DirectedGraphEdge[P] extends GraphEdge[P] {
      def source(): V
      def dest(): V
    }

    def findEdge(from: V, to: V): Option[E]
    def removeAllEdgesAndVertices(): Unit
    def deleteEdge(e: E): Unit
    def deleteVertex(v: V): Unit
    def leaves(): Set[V]
    def neighbors(v: V): Set[V]
    def precedes(v1: V, v2: V): Boolean
    def predecessors(v: V): Set[V]
    def isLeaf(v: V): Boolean
    def successors(v: V): Set[V]
    def outputEdgesOf(v: V): Set[E]
    def descendantsIntersectsSet(v: V, s: Set[V]): Boolean

    //    def collectDescendants(v: V): immutable.Set[V]
    //    def collectAncestors(v: V): immutable.Set[V]
    //    def collectAncestors(vs: Set[V]): immutable.Set[V]

    def _collectDescendants(v: V, result: mutable.Set[V]): Unit = {
      // inefficient
      if (!result.contains(v)) {
        result += v
        successors(v).map(_collectDescendants(_, result))
      }
    }

    def collectDescendants(v: V): immutable.Set[V] = {
      val result = mutable.Set[V]()
      _collectDescendants(v, result)
      result.toSet
    }

    // inefficient
    def _collectAncestors(v: V, result: mutable.Set[V]): Unit = {
      if (!result.contains(v)) {
        result += v
        predecessors(v).map(_collectAncestors(_, result))
      }
    }

    def collectAncestors(v: V): immutable.Set[V] = {
      val result = mutable.Set[V]()
      _collectAncestors(v, result)
      result.toSet
    }

    def collectAncestors(vs: Set[V]): immutable.Set[V] = {
      val result = mutable.Set[V]()
      vs.map(_collectAncestors(_, result))
      result.toSet
    }

    def removeInputs(vs: Set[V]): Unit
    def removeOutputs(vs: Set[V]): Unit
    def removeSuccessor(v: V, successor: V): Unit // TODO remove this method
    def removePredecessor(v: V, predecessor: V): Unit //TODO remove this method
    // def moralGraph(): UndirectedGraph[_, _] = null // TODO !!!
    def isAcyclic(): Boolean
    def shortestPath(source: V, goal: V): Option[List[E]]
  }

}