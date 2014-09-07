package axle.quanta

import axle.graph.DirectedGraph
import axle.algebra.Bijection
import spire.math.Rational
import spire.algebra.Field
import spire.algebra.Eq
import spire.implicits.eqOps
import spire.implicits.moduleOps
import spire.implicits.groupOps
import spire.implicits.multiplicativeGroupOps
import spire.implicits.multiplicativeSemigroupOps
import spire.implicits.additiveGroupOps
import spire.implicits.additiveSemigroupOps
import spire.implicits._

abstract class Area extends Quantum {
  def wikipediaUrl = "http://en.wikipedia.org/wiki/Area"
}

object Area extends Area {

  type Q = Area

  def units[N: Field: Eq] = List[UnitOfMeasurement[Q, N]](
    unit("m2", "m2"), // derive
    unit("km2", "km2"), // derive
    unit("cm2", "cm2") // derive
    )

  def links[N: Field: Eq] = {
    implicit val baseCG = cgnDisconnected[N]
    List[(UnitOfMeasurement[Q, N], UnitOfMeasurement[Q, N], Bijection[N, N])](
      (m2, km2, Scale10s(6)),
      (cm2, m2, Scale10s(6)))
  }

  def m2[N: Field: Eq](implicit cg: CG[N]) = byName(cg, "m2")
  def km2[N: Field: Eq](implicit cg: CG[N]) = byName(cg, "km2")
  def cm2[N: Field: Eq](implicit cg: CG[N]) = byName(cg, "cm2")

}
