package axle.quanta

import axle.algebra.Bijection
import axle.algebra.DirectedGraph
import spire.algebra.Eq
import spire.algebra.Field

case class Acceleration[N]() extends Quantum[N] {

  type Q = Acceleration[N]

  def wikipediaUrl: String = "http://en.wikipedia.org/wiki/Degree_(angle)"

  def unit(name: String, symbol: String, wiki: Option[String] = None) =
    UnitOfMeasurement[Acceleration[N], N](name, symbol, wiki)

  lazy val mpsps = unit("mps", "mps") // derive
  lazy val fpsps = unit("fps", "fps") // derive
  lazy val g = unit("g", "g", Some("http://en.wikipedia.org/wiki/Standard_gravity"))

  def units: List[UnitOfMeasurement[Acceleration[N], N]] =
    List(mpsps, fpsps, g)

  def links(implicit fn: Field[N]): Seq[(UnitOfMeasurement[Acceleration[N], N], UnitOfMeasurement[Acceleration[N], N], Bijection[N, N])] =
    List[(UnitOfMeasurement[Acceleration[N], N], UnitOfMeasurement[Acceleration[N], N], Bijection[N, N])](
      (mpsps, g, ScaleDouble(9.80665)))

}
