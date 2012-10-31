package axle.graph

trait GraphVertex[P] {
  def payload(): P
}

trait GraphEdge[P] {
  def payload(): P
}

trait GenGraph[VP, EP] {

  type V <: GraphVertex[VP]
  type E <: GraphEdge[EP]
  type S

  def storage(): S

  def size(): Int
  def edges(): Set[E]
  def vertices(): Set[V]
  def edge(v1: V, v2: V, ep: EP): (GenGraph[VP, EP], E)
  def +(vs: (V, V), ep: EP): (GenGraph[VP, EP], E) = edge(vs._1, vs._2, ep)
  def ++(eps: Seq[(V, V, EP)]): (GenGraph[VP, EP], Seq[E])

  def vertex(vp: VP): (GenGraph[VP, EP], V)
  def +(vp: VP): (GenGraph[VP, EP], V) = vertex(vp)
  def ++(vps: Seq[VP]): (GenGraph[VP, EP], Seq[V]) // TODO vps.map(vertex(_))

}

// CC[A, B] <: GenGraph[A, B]
// type Coll = CC[_, _]
// CC[A, B]

trait GraphFactory {
  def apply[A, B](): GenGraph[A, B]
  def apply[A, B](vps: Seq[A], ef: Seq[GraphVertex[A]] => Seq[(GraphVertex[A], GraphVertex[A], B)]): GenGraph[A, B]
}
