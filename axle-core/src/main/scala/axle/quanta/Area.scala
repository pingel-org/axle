package axle.quanta

import axle.algebra.Vertex
import axle.algebra.Bijection
import axle.algebra.DirectedGraph
import spire.algebra.Eq
import spire.algebra.Field
import spire.math.Rational
import spire.math.Real
import spire.implicits._

class Area[DG[_, _]: DirectedGraph] extends Quantum {

  def wikipediaUrl = "http://en.wikipedia.org/wiki/Area"

  type Q = this.type

  def units[N: Field: Eq] = List[UnitOfMeasurement[Q, N]](
    unit("m2", "m2"), // derive
    unit("km2", "km2"), // derive
    unit("cm2", "cm2") // derive
    )

  def links[N: Field: Eq] = {
    implicit val baseCG = cgnDisconnected[N, DG]
    List[(UnitOfMeasurement[Q, N], UnitOfMeasurement[Q, N], Bijection[N, N])](
      (m2, km2, Scale10s(6)),
      (cm2, m2, Scale10s(6)))
  }

  def m2[N: Field: Eq](implicit cg: CG[DG, N]) = byName(cg, "m2")
  def km2[N: Field: Eq](implicit cg: CG[DG, N]) = byName(cg, "km2")
  def cm2[N: Field: Eq](implicit cg: CG[DG, N]) = byName(cg, "cm2")

}
