package axle.graph

trait GraphVertex[VP] {
  def payload(): VP
}

trait GraphEdge[VP, EP] {
  def payload(): EP
}

trait GenGraph[VP, EP] {

  //  type V <: GraphVertex[VP]
  //  type E <: GraphEdge[VP, EP]
  //  type S

  def storage(): Any

  def size(): Int

  def edges(): Set[GraphEdge[VP, EP]]

  def vertices(): Set[GraphVertex[VP]]

  //  def edge(v1: V, v2: V, ep: EP): (GenGraph[VP, EP], E)
  //  def +(vs: (V, V), ep: EP): (GenGraph[VP, EP], E) = edge(vs._1, vs._2, ep)
  //  def vertex(vp: VP): (GenGraph[VP, EP], V)
  //  def +(vp: VP): (GenGraph[VP, EP], V) = vertex(vp)

  def findVertex(test: VP => Boolean): Option[GraphVertex[VP]]

}

// CC[A, B] <: GenGraph[A, B]
// type Coll = CC[_, _]
// CC[A, B]

trait GraphFactory {
  def apply[A, B](vps: Seq[A], ef: Seq[GraphVertex[A]] => Seq[(GraphVertex[A], GraphVertex[A], B)]): GenGraph[A, B]
}
