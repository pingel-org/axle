
package org.pingel.axle.graph {

  import scala.collection._

  trait Graph {

    trait GraphVertex {
      def getPayload(): VP
      type VP // Payload
    }

    trait GraphEdge {
      def getPayload(): EP
      type EP // Payload
    }

    type V <: GraphVertex
    type E <: GraphEdge
    
    var vertices = Set[V]()
    var edges = Set[E]()

    def size() = vertices.size

    def getEdges() = edges

    def getVertices() = vertices

    def newEdge(v1: V, v2: V, ep: E#EP): E

    def +=(vs: (V, V), ep: E#EP): E = newEdge(vs._1, vs._2, ep)

    def newVertex(vp: V#VP): V

    def +=(vp: V#VP): V = newVertex(vp)

  }

}

// http://ctp.di.fct.unl.pt/~amd/pmp/teoricas/10.html
