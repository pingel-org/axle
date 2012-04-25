package org.pingel.axle.graph

trait DirectedGraphFactory extends GraphFactory {

  import scala.collection._

  type G[VP, EP] <: DirectedGraph[VP, EP]

  trait DirectedGraph[VP, EP] extends Graph[VP, EP] {

    type V <: DirectedGraphVertex
    type E <: DirectedGraphEdge

    trait DirectedGraphVertex extends GraphVertex

    trait DirectedGraphEdge extends GraphEdge {
      def getSource(): V
      def getDest(): V
    }

    def getEdge(from: V, to: V): Option[E]
    def removeAllEdgesAndVertices(): Unit
    def deleteEdge(e: E): Unit
    def deleteVertex(v: V): Unit
    def getLeaves(): Set[V]
    def getNeighbors(v: V): Set[V]
    def precedes(v1: V, v2: V): Boolean
    def getPredecessors(v: V): Set[V]
    def isLeaf(v: V): Boolean
    def getSuccessors(v: V): Set[V]
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

//  class SimpleDirectedGraph() extends DirectedGraph {
//
//    type V = SimpleDirectedVertex
//    type E = SimpleDirectedEdge
//
//    class SimpleDirectedVertex(label: String) extends DirectedGraphVertex {
//      type VP = String
//      def getPayload() = label
//    }
//
//    def newVertex(vp: String) = {
//      val v = new SimpleDirectedVertex(vp)
//      addVertex(v)
//      v
//    }
//
//    class SimpleDirectedEdge(v1: SimpleDirectedVertex, v2: SimpleDirectedVertex, ep: E#EP) extends DirectedGraphEdge {
//      def getSource() = v1
//      def getDest() = v2
//      type EP = String
//      def getPayload() = ep
//    }
//
//    def newEdge(v1: SimpleDirectedVertex, v2: SimpleDirectedVertex, ep: E#EP) = {
//      val result = new SimpleDirectedEdge(v1, v2, ep)
//      addEdge(result)
//      result
//    }
//  }

}