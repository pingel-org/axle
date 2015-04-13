package axle.algebra

import scala.annotation.implicitNotFound
import spire.algebra.Eq
import spire.implicits.eqOps

// Note: ES includes the vertices in order to provide uniquess for jung
// This could also be accomplished by making Edge not a case class

@implicitNotFound("Witness not found for UndirectedEge[${VP}, ${EP}]")
trait UndirectedEdge[VP, EP] {

  def vertices: (Vertex[VP], Vertex[VP])

  def payload: EP
}

@implicitNotFound("Witness not found for DirectedEdge[${VP}, ${EP}]")
trait DirectedEdge[VP, EP] {

  def from: Vertex[VP]

  def to: Vertex[VP]

  def payload: EP
}

case class Edge[S, EP: Eq](storage: S, payloadF: S => EP) {
  def payload: EP = payloadF(storage)
}

object Edge {
  implicit def edgeEq[S, EP](): Eq[Edge[S, EP]] = new Eq[Edge[S, EP]] {
    def eqv(x: Edge[S, EP], y: Edge[S, EP]): Boolean = x equals y // TODO
  }
}