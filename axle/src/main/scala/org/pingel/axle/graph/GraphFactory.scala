package org.pingel.axle.graph

trait GraphFactory {

  type G[VP, EP] <: Graph[VP, EP]

  def graph[A, B](): G[A, B]

  trait Graph[VP, EP] {

    type V <: GraphVertex[VP]
    type E <: GraphEdge[EP]
    type S

    def getStorage: S

    trait GraphVertex[P] {
      def getPayload(): P
    }

    trait GraphEdge[P] {
      def getPayload(): P
    }

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