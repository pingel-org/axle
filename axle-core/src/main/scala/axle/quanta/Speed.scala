package axle.quanta

import axle.graph.DirectedGraph
import spire.algebra._
import spire.math.Rational
import spire.implicits.eqOps
import spire.implicits.moduleOps
import spire.implicits.groupOps
import spire.implicits.multiplicativeGroupOps
import spire.implicits.multiplicativeSemigroupOps
import spire.implicits.additiveGroupOps
import spire.implicits.additiveSemigroupOps

abstract class Speed extends Quantum {
  def wikipediaUrl = "http://en.wikipedia.org/wiki/Speed"
}

object Speed extends Speed {

  type Q = Speed

  def units[N: Field: Eq] = List[UnitOfMeasurement[Q, N]](
    unit("mps", "mps"), // derive
    unit("fps", "fps"), // derive
    unit("mph", "mph"), // derive
    unit("kph", "kph"), // derive
    unit("knot", "kn", Some("http://en.wikipedia.org/wiki/Knot_(unit)")),
    unit("Light Speed", "c", Some("http://en.wikipedia.org/wiki/Speed_of_light")),
    unit("Speed limit", "speed limit"))

  def links[N: Field: Eq] = {
    implicit val baseCG = cgnDisconnected[N]
    List[(UnitOfMeasurement[Q, N], UnitOfMeasurement[Q, N], N => N, N => N)](
      (knot, kph, _ * 1.852, _ / 1.852),
      (mps, c, _ * 299792458, _ / 299792458),
      (mph, speedLimit, _ * 65, _ / 65))
  }

  def mps[N: Field: Eq](implicit cg: CG[N]) = byName(cg, "mps")
  def fps[N: Field: Eq](implicit cg: CG[N]) = byName(cg, "fps")
  def mph[N: Field: Eq](implicit cg: CG[N]) = byName(cg, "mph")
  def kph[N: Field: Eq](implicit cg: CG[N]) = byName(cg, "kph")
  def knot[N: Field: Eq](implicit cg: CG[N]) = byName(cg, "knot")
  def kn[N: Field: Eq](implicit cg: CG[N]) = byName(cg, "knot")
  def c[N: Field: Eq](implicit cg: CG[N]) = byName(cg, "Light Speed")
  def speedLimit[N: Field: Eq](implicit cg: CG[N]) = byName(cg, "Speed Limit")

}
