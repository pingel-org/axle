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
    def collectDescendants(v: V, result: mutable.Set[V]): Unit
    def collectAncestors(v: V, result: mutable.Set[V]): Unit
    def collectAncestors(vs: Set[V], result: mutable.Set[V]): Unit
    def removeInputs(vs: Set[V]): Unit
    def removeOutputs(vs: Set[V]): Unit
    def removeSuccessor(v: V, successor: V): Unit // TODO remove this method
    def removePredecessor(v: V, predecessor: V): Unit //TODO remove this method
    // def moralGraph(): UndirectedGraph[_, _] = null // TODO !!!
    def isAcyclic(): Boolean
    def shortestPath(source: V, goal: V): Option[List[E]]
  }

}