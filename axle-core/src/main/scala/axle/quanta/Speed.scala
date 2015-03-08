package axle.quanta

import axle.algebra.Bijection
import axle.algebra.DirectedGraph
import spire.algebra.Eq
import spire.algebra.Field

case class Speed[N]() extends Quantum[N] {

  type Q = Speed[N]

  def wikipediaUrl: String = "http://en.wikipedia.org/wiki/Speed"

  def unit(name: String, symbol: String, wiki: Option[String] = None) =
    UnitOfMeasurement[Speed[N], N](name, symbol, wiki)

  lazy val mps = unit("mps", "mps") // derive
  lazy val fps = unit("fps", "fps") // derive
  lazy val mph = unit("mph", "mph") // derive
  lazy val kph = unit("kph", "kph") // derive
  lazy val knot = unit("knot", "kn", Some("http://en.wikipedia.org/wiki/Knot_(unit)"))
  lazy val kn = knot
  lazy val c = unit("Light Speed", "c", Some("http://en.wikipedia.org/wiki/Speed_of_light"))
  lazy val speedLimit = unit("Speed limit", "speed limit")

  def units: List[UnitOfMeasurement[Speed[N], N]] =
    List(mps, fps, mph, kph, knot, c, speedLimit)

  def links(implicit fn: Field[N]): Seq[(UnitOfMeasurement[Speed[N], N], UnitOfMeasurement[Speed[N], N], Bijection[N, N])] =
    List[(UnitOfMeasurement[Speed[N], N], UnitOfMeasurement[Speed[N], N], Bijection[N, N])](
      (knot, kph, ScaleDouble(1.852)),
      (mps, c, ScaleInt(299792458)),
      (mph, speedLimit, ScaleInt(65)))

}
