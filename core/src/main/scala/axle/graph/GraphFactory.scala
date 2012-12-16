package axle.graph


case class Edge[EP](payload: EP)

case class Vertex[VP](payload: VP)

/*

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

// G <: GenGraph[_, _], GV <: GraphVertex[_], GE <: GraphEdge[_, _]
trait GraphFactory {

  def apply[VP, EP](vps: Seq[VP], ef: Seq[GraphVertex[VP]] => Seq[(GraphVertex[VP], GraphVertex[VP], GraphEdge[VP, EP])]): GenGraph[VP, EP]

}
*/
