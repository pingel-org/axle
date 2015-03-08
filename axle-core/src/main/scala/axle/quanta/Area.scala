package axle.quanta

import axle.algebra.Bijection
import axle.algebra.DirectedGraph
import spire.algebra.Eq
import spire.algebra.Field

case class Area[N]() extends Quantum[N] {

  type Q = Area[N]

  def wikipediaUrl: String = "http://en.wikipedia.org/wiki/Area"

  def unit(name: String, symbol: String, wiki: Option[String] = None) =
    UnitOfMeasurement[Area[N], N](name, symbol, wiki)

  lazy val m2 = unit("m2", "m2") // derive
  lazy val km2 = unit("km2", "km2") // derive
  lazy val cm2 = unit("cm2", "cm2") // derive

  def units: List[UnitOfMeasurement[Area[N], N]] =
    List(m2, km2, cm2)

  def links(implicit fn: Field[N]): Seq[(UnitOfMeasurement[Area[N], N], UnitOfMeasurement[Area[N], N], Bijection[N, N])] =
    List[(UnitOfMeasurement[Area[N], N], UnitOfMeasurement[Area[N], N], Bijection[N, N])](
      (m2, km2, Scale10s(6)),
      (cm2, m2, Scale10s(6)))

}
