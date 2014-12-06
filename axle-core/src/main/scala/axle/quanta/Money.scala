package axle.quanta

import axle.algebra.Vertex
import axle.algebra.Bijection
import axle.algebra.DirectedGraph
import spire.algebra.Eq
import spire.algebra.Field
import spire.math.Rational
import spire.math.Real
import spire.implicits._

class Money[DG[_, _]: DirectedGraph] extends Quantum {
  
  def wikipediaUrl = "http://en.wikipedia.org/wiki/Money"

  type Q = this.type

  def units[N: Field: Eq] = List[UnitOfMeasurement[Q, N]](
    unit("US Dollar", "USD"))

  def links[N: Field: Eq] = {
    implicit val baseCG = cgnDisconnected[N, DG]
    List[(UnitOfMeasurement[Q, N], UnitOfMeasurement[Q, N], Bijection[N, N])]()
  }

  // def x[N: Field: Eq](implicit cg: CG[DG, N]) = byName(cg, "x")

}
