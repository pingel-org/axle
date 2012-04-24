package org.pingel.axle.graph

trait UndirectedGraphFactory extends GraphFactory {

  import scala.collection._

  trait UndirectedGraph[VP, EP] extends Graph[VP, EP] {

    type V <: UndirectedGraphVertex
    type E <: UndirectedGraphEdge

    trait UndirectedGraphVertex extends GraphVertex

    trait UndirectedGraphEdge extends GraphEdge {

      def getVertices(): (V, V)

      def other(u: V): V = {
        val (v1, v2) = getVertices()
        u match {
          case _ if u.equals(v1) => v2
          case _ if u.equals(v2) => v1
          case _ => throw new Exception("can't find 'other' of a vertex that isn't on the edge itself")
        }
      }

      def connects(a1: V, a2: V) = {
        val (v1, v2) = getVertices()
        (v1 == a1 && v2 == a2) || (v2 == a1 && v1 == a2)
      }
    }

    
    def vertex(payload: VP): V
    
    def edge(v1: V, v2: V, payload: EP): E

    def unlink(e: E): Unit

    def unlink(v1: V, v2: V): Unit

    def areNeighbors(v1: V, v2: V): Boolean

    
    def isClique(vs: Set[V]): Boolean = {
      // vs.pairs().forall({ case (a, b) => ( (a == b) || areNeighbors(a, b) ) })
      var vList = mutable.ArrayBuffer[V]()
      vList ++= vs
      for (i <- 0 until vList.size) {
        for (j <- 0 until vList.size) {
          if (!areNeighbors(vList(i), vList(j))) {
            return false
          }
        }
      }
      true
    }

    def getNumEdgesToForceClique(vs: Set[V], payload: (V, V) => EP) = {

      var N = mutable.ArrayBuffer[V]()
      N ++= vs

      var result = 0

      for (i <- 0 to (N.size - 2)) {
        val vi = N(i)
        for (j <- (i + 1) until N.size) {
          val vj = N(j)
          if (!areNeighbors(vi, vj)) {
            edge(vi, vj, payload(vi, vj))
            result += 1
          }
        }
      }

      result
    }

    def forceClique(vs: Set[V], payload: (V, V) => EP) {

      var vList = mutable.ArrayBuffer[V]()
      vList ++= vs

      for (i <- 0 until (vList.size - 1)) {
        val vi = vList(i)
        for (j <- (i + 1) until vList.size) {
          val vj = vList(j)
          if (!areNeighbors(vi, vj)) {
            edge(vi, vj, payload(vi, vj))
          }
        }
      }

    }

    def vertexWithFewestEdgesToEliminateAmong(among: Set[V], payload: (V, V) => EP): Option[V] = {

      // assert: among is a subset of vertices

      var result: Option[V] = None
      var minSoFar = Integer.MAX_VALUE

      for (v <- among) {
        val x = getNumEdgesToForceClique(getNeighbors(v), payload)
        if (result == None) {
          result = Some(v)
          minSoFar = x
        } else if (x < minSoFar) {
          result = Some(v)
          minSoFar = x
        }
      }
      result
    }

    def vertexWithFewestNeighborsAmong(among: Set[V]): Option[V] = {
      // assert: among is a subset of vertices

      var result: Option[V] = None
      var minSoFar = Integer.MAX_VALUE

      for (v <- among) {
        val x = getNeighbors(v).size
        if (result == None) {
          result = Some(v)
          minSoFar = x
        } else if (x < minSoFar) {
          result = Some(v)
          minSoFar = x
        }
      }

      result
    }

    def degree(v: V): Int

    def getEdges(v: V): Set[E]

    def getNeighbors(v: V): Set[V]

    def delete(v: V): Unit

    // a "leaf" is vertex with only one neighbor
    def firstLeafOtherThan(r: V): Option[V]

    def eliminate(v: V, payload: (V, V) => EP): Unit

    def eliminate(vs: List[V], payload: (V, V) => EP): Unit

  }

//  class SimpleGraph() extends UndirectedGraph {
//
//    type V = SimpleVertex
//    type E = SimpleEdge
//
//    class SimpleVertex(vp: String) extends UndirectedGraphVertex {
//      type VP = String
//      def getPayload() = vp
//    }
//
//    def vertex(name: String) = {
//      val v = new SimpleVertex(name)
//      addVertex(v)
//      v
//    }
//
//    class SimpleEdge(v1: SimpleVertex, v2: SimpleVertex, ep: String) extends UndirectedGraphEdge {
//      type EP = String
//      def getVertices() = (v1, v2)
//      def getPayload() = ep
//    }
//
//    def edge(v1: SimpleVertex, v2: SimpleVertex, ep: String) = {
//      val result = new SimpleEdge(v1, v2, ep)
//      addEdge(result)
//      result
//    }
//  }
  
  
}