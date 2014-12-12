package axle.quanta

import axle.algebra.Bijection
import axle.algebra.DirectedGraph
import spire.algebra.Eq
import spire.algebra.Field

case object Area extends Quantum {

  type Q = Area.type

  def wikipediaUrl: String = "http://en.wikipedia.org/wiki/Area"

  def unit[N](name: String, symbol: String, wiki: Option[String] = None) =
    UnitOfMeasurement[Q, N](name, symbol, wiki)

  def m2[N]: UnitOfMeasurement[Q, N] = unit("m2", "m2") // derive
  def km2[N]: UnitOfMeasurement[Q, N] = unit("km2", "km2") // derive
  def cm2[N]: UnitOfMeasurement[Q, N] = unit("cm2", "cm2") // derive

  def units[N]: List[UnitOfMeasurement[Q, N]] =
    List(m2, km2, cm2)

  def links[N: Field]: Seq[(UnitOfMeasurement[Q, N], UnitOfMeasurement[Q, N], Bijection[N, N])] =
    List[(UnitOfMeasurement[Q, N], UnitOfMeasurement[Q, N], Bijection[N, N])](
      (m2, km2, Scale10s(6)),
      (cm2, m2, Scale10s(6)))

}
