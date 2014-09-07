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

abstract class Force extends Quantum {
  def wikipediaUrl = "http://en.wikipedia.org/wiki/Force"
}

object Force extends Force {

  type Q = Force

  def units[N: Field: Eq] = List[UnitOfMeasurement[Q, N]](
    unit("pound", "lb", Some("http://en.wikipedia.org/wiki/Pound-force")),
    unit("newton", "N", Some("http://en.wikipedia.org/wiki/Newton_(unit)")),
    unit("dyne", "dyn", Some("http://en.wikipedia.org/wiki/Dyne")))

  def links[N: Field: Eq] = {
    implicit val baseCG = cgnDisconnected[N]
    List[(UnitOfMeasurement[Q, N], UnitOfMeasurement[Q, N], Bijection[N, N])]()
  }

  def pound[N: Field: Eq](implicit cg: CG[N]) = byName(cg, "pound")
  def newton[N: Field: Eq](implicit cg: CG[N]) = byName(cg, "newton")
  def dyne[N: Field: Eq](implicit cg: CG[N]) = byName(cg, "dyne")

}
