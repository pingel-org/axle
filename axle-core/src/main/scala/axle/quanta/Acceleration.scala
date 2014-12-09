package axle.quanta

import axle.algebra.Vertex
import axle.algebra.Bijection
import axle.algebra.DirectedGraph
import spire.algebra.Eq
import spire.algebra.Field
import spire.math.Rational
import spire.math.Real
import spire.implicits._

//class Acceleration[DG[_, _]: DirectedGraph] extends Quantum {
//
//  def wikipediaUrl = "http://en.wikipedia.org/wiki/Acceleration"
//
//  type Q = this.type
//
//  def units[N: Field: Eq] = List[UnitOfMeasurement[Q, N]](
//    unit("mps", "mps"), // derive
//    unit("fps", "fps"), // derive
//    unit("g", "g", Some("http://en.wikipedia.org/wiki/Standard_gravity")))
//
//  def links[N: Field: Eq]: Seq[(UnitOfMeasurement[Q, N], UnitOfMeasurement[Q, N], Bijection[N, N])] = {
//
//    implicit val baseCG = cgnDisconnected[N, DG]
//
//    List[(UnitOfMeasurement[Q, N], UnitOfMeasurement[Q, N], Bijection[N, N])](
//      (mpsps, g, ScaleDouble(9.80665)))
//  }
//
//  def mpsps[N: Field: Eq](implicit cg: CG[DG, N]) = byName(cg, "mpsps")
//
//  def fpsps[N: Field: Eq](implicit cg: CG[DG, N]) = byName(cg, "fpsps")
//
//  def g[N: Field: Eq](implicit cg: CG[DG, N]) = byName(cg, "g")
//
//}
