package axle.quanta

import axle.algebra.Vertex
import axle.algebra.Bijection
import axle.algebra.DirectedGraph
import spire.algebra.Eq
import spire.algebra.Field
import spire.math.Rational
import spire.math.Real
import spire.implicits._

class Frequency[DG[_, _]: DirectedGraph] extends Quantum {
  
  def wikipediaUrl = "http://en.wikipedia.org/wiki/Frequency"

  type Q = this.type

  def units[N: Field: Eq] = List[UnitOfMeasurement[Q, N]](
    unit("Hertz", "Hz", Some("http://en.wikipedia.org/wiki/Hertz")),
    unit("Kilohertz", "KHz"),
    unit("Megahertz", "MHz"),
    unit("Gigahertz", "GHz"))

  def links[N: Field: Eq] = {
    implicit val baseCG = cgnDisconnected[N, DG]
    List[(UnitOfMeasurement[Q, N], UnitOfMeasurement[Q, N], Bijection[N, N])](
      (Hz, KHz, Scale10s(3)),
      (Hz, MHz, Scale10s(9)),
      (Hz, GHz, Scale10s(12)))
  }

  def hertz[N: Field: Eq](implicit cg: CG[DG, N]) = byName(cg, "hertz")
  def Hz[N: Field: Eq](implicit cg: CG[DG, N]) = byName(cg, "Hz")
  def kilohertz[N: Field: Eq](implicit cg: CG[DG, N]) = byName(cg, "kilohertz")
  def KHz[N: Field: Eq](implicit cg: CG[DG, N]) = byName(cg, "KHz")
  def megahertz[N: Field: Eq](implicit cg: CG[DG, N]) = byName(cg, "megahertz")
  def MHz[N: Field: Eq](implicit cg: CG[DG, N]) = byName(cg, "MHz")
  def gigahertz[N: Field: Eq](implicit cg: CG[DG, N]) = byName(cg, "gigahertz")
  def GHz[N: Field: Eq](implicit cg: CG[DG, N]) = byName(cg, "GHz")

}
