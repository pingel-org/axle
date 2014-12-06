package axle.quanta

import axle.algebra.Vertex
import axle.algebra.Bijection
import axle.algebra.DirectedGraph
import spire.algebra.Eq
import spire.algebra.Field
import spire.math.Rational
import spire.math.Real
import spire.implicits._

class Energy[DG[_, _]: DirectedGraph] extends Quantum {
  
  def wikipediaUrl = "http://en.wikipedia.org/wiki/Energy"

  type Q = this.type

  def units[N: Field: Eq] = List[UnitOfMeasurement[Q, N]](
    unit("kwh", "kwh"), // derive
    unit("joule", "J"),
    unit("kilojoule", "KJ"),
    unit("megajoule", "MJ"),
    unit("ton TNT", "T", Some("http://en.wikipedia.org/wiki/TNT_equivalent")),
    unit("kiloton", "KT"),
    unit("megaton", "MT"),
    unit("gigaton", "GT"))

  def links[N: Field: Eq] = {
    implicit val baseCG = cgnDisconnected[N, DG]
    List[(UnitOfMeasurement[Q, N], UnitOfMeasurement[Q, N], Bijection[N, N])](
      (megajoule, t, ScaleDouble(4.184)),
      (joule, kilojoule, Scale10s(3)),
      (joule, megajoule, Scale10s(6)),
      (t, kt, Scale10s(3)),
      (t, mt, Scale10s(6)),
      (t, gt, Scale10s(9)))
  }

  def kwh[N: Field: Eq](implicit cg: CG[DG, N]) = byName(cg, "kwh")
  def joule[N: Field: Eq](implicit cg: CG[DG, N]) = byName(cg, "joule")
  def kilojoule[N: Field: Eq](implicit cg: CG[DG, N]) = byName(cg, "kilojoule")
  def megajoule[N: Field: Eq](implicit cg: CG[DG, N]) = byName(cg, "megajoule")
  def tonTNT[N: Field: Eq](implicit cg: CG[DG, N]) = byName(cg, "tonTNT")
  def t[N: Field: Eq](implicit cg: CG[DG, N]) = byName(cg, "tonTNT")
  def kiloton[N: Field: Eq](implicit cg: CG[DG, N]) = byName(cg, "kiloton")
  def kt[N: Field: Eq](implicit cg: CG[DG, N]) = byName(cg, "kiloton")
  def megaton[N: Field: Eq](implicit cg: CG[DG, N]) = byName(cg, "megaton")
  def mt[N: Field: Eq](implicit cg: CG[DG, N]) = byName(cg, "megaton")
  def gt[N: Field: Eq](implicit cg: CG[DG, N]) = byName(cg, "gigaton")

  // TODO lazy val castleBravo = 15 *: megaton // Some("Castle Bravo Thermonuclear Bomb"), None, Some("http://en.wikipedia.org/wiki/Castle_Bravo"))

}
