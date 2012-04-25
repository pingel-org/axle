package org.pingel.axle.graph

trait GraphFactory {

  type G[VP, EP] <: Graph[VP, EP]

  def graph[VP, EP](): G[VP, EP]

  trait Graph[VP, EP] {

    type S

    def getStorage: S

    trait GraphVertex {
      def getPayload(): VP
    }

    trait GraphEdge {
      def getPayload(): EP
    }

    type V <: GraphVertex
    type E <: GraphEdge

    def size(): Int

    def getEdges(): Set[E]

    def getVertices(): Set[V]

    def edge(v1: V, v2: V, ep: EP): E

    def +=(vs: (V, V), ep: EP): E = edge(vs._1, vs._2, ep)

    def vertex(vp: VP): V

    def +=(vp: VP): V = vertex(vp)

    // TODO // def dup(): G

    def draw(): Unit
    
  }

}