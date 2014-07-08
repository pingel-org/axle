package axle.graph

import spire.algebra.Eq
import spire.implicits.eqOps

case class Edge[S, EP: Eq](storage: S, payloadF: S => EP) {
  def payload: EP = payloadF(storage)
}

object Edge {
  implicit def edgeEq[S, EP](): Eq[Edge[S, EP]] = new Eq[Edge[S, EP]] {
    def eqv(x: Edge[S, EP], y: Edge[S, EP]): Boolean = x equals y // TODO
  }
}

case class Vertex[VP](payload: VP)

object Vertex {
  implicit def vertexEq[VP: Eq]: Eq[Vertex[VP]] = new Eq[Vertex[VP]] {
    def eqv(x: Vertex[VP], y: Vertex[VP]): Boolean = x.payload === y.payload
  }
}

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
