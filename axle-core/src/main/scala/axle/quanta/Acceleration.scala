package axle.quanta

import axle.algebra.Bijection
import axle.algebra.DirectedGraph
import spire.algebra.Eq
import spire.algebra.Field

case object Acceleration extends Quantum {

  type Q = Acceleration.type

  def wikipediaUrl: String = "http://en.wikipedia.org/wiki/Degree_(angle)"

  def unit[N](name: String, symbol: String, wiki: Option[String] = None) =
    UnitOfMeasurement[Q, N](name, symbol, wiki)

  def mpsps[N]: UnitOfMeasurement[Q, N] = unit("mps", "mps") // derive
  def fpsps[N]: UnitOfMeasurement[Q, N] = unit("fps", "fps") // derive
  def g[N]: UnitOfMeasurement[Q, N] = unit("g", "g", Some("http://en.wikipedia.org/wiki/Standard_gravity"))

  def units[N]: List[UnitOfMeasurement[Q, N]] =
    List(mpsps, fpsps, g)

  def links[N: Field]: Seq[(UnitOfMeasurement[Q, N], UnitOfMeasurement[Q, N], Bijection[N, N])] =
    List[(UnitOfMeasurement[Q, N], UnitOfMeasurement[Q, N], Bijection[N, N])](
      (mpsps, g, ScaleDouble(9.80665)))

}
