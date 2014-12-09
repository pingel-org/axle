package axle.quanta

import axle.algebra.Vertex
import axle.algebra.Bijection
import axle.algebra.DirectedGraph
import spire.algebra.Eq
import spire.algebra.Field
import spire.math.Rational
import spire.math.Real
import spire.implicits._

//class Force[DG[_, _]: DirectedGraph] extends Quantum {
//
//  def wikipediaUrl = "http://en.wikipedia.org/wiki/Force"
//
//  type Q = this.type
//
//  def units[N: Field: Eq] = List[UnitOfMeasurement[Q, N]](
//    unit("pound", "lb", Some("http://en.wikipedia.org/wiki/Pound-force")),
//    unit("newton", "N", Some("http://en.wikipedia.org/wiki/Newton_(unit)")),
//    unit("dyne", "dyn", Some("http://en.wikipedia.org/wiki/Dyne")))
//
//  def links[N: Field: Eq] = {
//    implicit val baseCG = cgnDisconnected[N, DG]
//    List[(UnitOfMeasurement[Q, N], UnitOfMeasurement[Q, N], Bijection[N, N])]()
//  }
//
//  def pound[N: Field: Eq](implicit cg: CG[DG, N]) = byName(cg, "pound")
//
//  def newton[N: Field: Eq](implicit cg: CG[DG, N]) = byName(cg, "newton")
//
//  def dyne[N: Field: Eq](implicit cg: CG[DG, N]) = byName(cg, "dyne")
//
//}
