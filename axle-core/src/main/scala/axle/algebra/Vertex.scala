package axle.algebra

import spire.algebra.Eq
import spire.implicits.eqOps

case class Vertex[VP](payload: VP)

object Vertex {
  implicit def vertexEq[VP: Eq]: Eq[Vertex[VP]] = new Eq[Vertex[VP]] {
    def eqv(x: Vertex[VP], y: Vertex[VP]): Boolean = x.payload === y.payload
  }
}
