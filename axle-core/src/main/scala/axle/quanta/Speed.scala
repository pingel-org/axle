package axle.quanta

import axle.algebra.Bijection
import axle.algebra.DirectedGraph
import spire.algebra.Eq
import spire.algebra.Field

case class Speed[N]() extends Quantum4[N] {

  type Q = Speed[N]

  def wikipediaUrl: String = "http://en.wikipedia.org/wiki/Speed"

  def unit(name: String, symbol: String, wiki: Option[String] = None) =
    UnitOfMeasurement4[Q, N](name, symbol, wiki)

  def mps: UnitOfMeasurement4[Q, N] = unit("mps", "mps") // derive
  def fps: UnitOfMeasurement4[Q, N] = unit("fps", "fps") // derive
  def mph: UnitOfMeasurement4[Q, N] = unit("mph", "mph") // derive
  def kph: UnitOfMeasurement4[Q, N] = unit("kph", "kph") // derive
  def knot: UnitOfMeasurement4[Q, N] = unit("knot", "kn", Some("http://en.wikipedia.org/wiki/Knot_(unit)"))
  def kn = knot
  def c: UnitOfMeasurement4[Q, N] = unit("Light Speed", "c", Some("http://en.wikipedia.org/wiki/Speed_of_light"))
  def speedLimit: UnitOfMeasurement4[Q, N] = unit("Speed limit", "speed limit")

  def units: List[UnitOfMeasurement4[Q, N]] =
    List(mps, fps, mph, kph, knot, c, speedLimit)

  def links(implicit fn: Field[N]): Seq[(UnitOfMeasurement4[Q, N], UnitOfMeasurement4[Q, N], Bijection[N, N])] =
    List[(UnitOfMeasurement4[Q, N], UnitOfMeasurement4[Q, N], Bijection[N, N])](
      (knot, kph, ScaleDouble(1.852)),
      (mps, c, ScaleInt(299792458)),
      (mph, speedLimit, ScaleInt(65)))

}
