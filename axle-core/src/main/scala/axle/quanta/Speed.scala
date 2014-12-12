package axle.quanta

import axle.algebra.Bijection
import axle.algebra.DirectedGraph
import spire.algebra.Eq
import spire.algebra.Field

case object Speed extends Quantum {

  type Q = Speed.type

  def wikipediaUrl: String = "http://en.wikipedia.org/wiki/Speed"

  def unit[N](name: String, symbol: String, wiki: Option[String] = None) =
    UnitOfMeasurement[Q, N](name, symbol, wiki)

  def mps[N]: UnitOfMeasurement[Q, N] = unit("mps", "mps") // derive
  def fps[N]: UnitOfMeasurement[Q, N] = unit("fps", "fps") // derive
  def mph[N]: UnitOfMeasurement[Q, N] = unit("mph", "mph") // derive
  def kph[N]: UnitOfMeasurement[Q, N] = unit("kph", "kph") // derive
  def knot[N]: UnitOfMeasurement[Q, N] = unit("knot", "kn", Some("http://en.wikipedia.org/wiki/Knot_(unit)"))
  def kn[N] = knot[N]
  def c[N]: UnitOfMeasurement[Q, N] = unit("Light Speed", "c", Some("http://en.wikipedia.org/wiki/Speed_of_light"))
  def speedLimit[N]: UnitOfMeasurement[Q, N] = unit("Speed limit", "speed limit")

  def units[N]: List[UnitOfMeasurement[Q, N]] =
    List(mps, fps, mph, kph, knot, c, speedLimit)

  def links[N: Field]: Seq[(UnitOfMeasurement[Q, N], UnitOfMeasurement[Q, N], Bijection[N, N])] =
    List[(UnitOfMeasurement[Q, N], UnitOfMeasurement[Q, N], Bijection[N, N])](
      (knot, kph, ScaleDouble(1.852)),
      (mps, c, ScaleInt(299792458)),
      (mph, speedLimit, ScaleInt(65)))

  //  implicit def conversionGraph[N: Field: Eq, DG[_, _]: DirectedGraph] =
  //    cgn(units[N], links)

}
