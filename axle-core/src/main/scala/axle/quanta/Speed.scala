package axle.quanta

import axle.algebra.Vertex
import axle.algebra.Bijection
import axle.algebra.DirectedGraph
import spire.algebra.Eq
import spire.algebra.Field
import spire.math.Rational
import spire.math.Real
import spire.implicits._

class Speed[DG[_, _]: DirectedGraph] extends Quantum {

  def wikipediaUrl = "http://en.wikipedia.org/wiki/Speed"

  type Q = this.type

  def units[N: Field: Eq] = List[UnitOfMeasurement[Q, N]](
    unit("mps", "mps"), // derive
    unit("fps", "fps"), // derive
    unit("mph", "mph"), // derive
    unit("kph", "kph"), // derive
    unit("knot", "kn", Some("http://en.wikipedia.org/wiki/Knot_(unit)")),
    unit("Light Speed", "c", Some("http://en.wikipedia.org/wiki/Speed_of_light")),
    unit("Speed limit", "speed limit"))

  def links[N: Field: Eq] = {
    implicit val baseCG = cgnDisconnected[N, DG]
    List[(UnitOfMeasurement[Q, N], UnitOfMeasurement[Q, N], Bijection[N, N])](
      (knot, kph, ScaleDouble(1.852)),
      (mps, c, ScaleInt(299792458)),
      (mph, speedLimit, ScaleInt(65)))
  }

  def mps[N: Field: Eq](implicit cg: CG[DG, N]) = byName(cg, "mps")
  def fps[N: Field: Eq](implicit cg: CG[DG, N]) = byName(cg, "fps")
  def mph[N: Field: Eq](implicit cg: CG[DG, N]) = byName(cg, "mph")
  def kph[N: Field: Eq](implicit cg: CG[DG, N]) = byName(cg, "kph")
  def knot[N: Field: Eq](implicit cg: CG[DG, N]) = byName(cg, "knot")
  def kn[N: Field: Eq](implicit cg: CG[DG, N]) = byName(cg, "knot")
  def c[N: Field: Eq](implicit cg: CG[DG, N]) = byName(cg, "Light Speed")
  def speedLimit[N: Field: Eq](implicit cg: CG[DG, N]) = byName(cg, "Speed Limit")

}
