package axle.graph

trait GraphFactory {

  type G[VP, EP] <: Graph[VP, EP]

  def graph[A, B](): G[A, B]

  trait Graph[VP, EP] {

    type V <: GraphVertex[VP]
    type E <: GraphEdge[EP]
    type S

    def storage: S

    trait GraphVertex[P] {
      def payload(): P
    }

    trait GraphEdge[P] {
      def payload(): P
    }

    def size(): Int
    def edges(): Set[E]
    def vertices(): Set[V]
    def edge(v1: V, v2: V, ep: EP): E
    def +=(vs: (V, V), ep: EP): E = edge(vs._1, vs._2, ep)
    def vertex(vp: VP): V
    def +=(vp: VP): V = vertex(vp)
    def ++=(vps: Seq[VP]) = vps.map(vertex(_))

  }

}