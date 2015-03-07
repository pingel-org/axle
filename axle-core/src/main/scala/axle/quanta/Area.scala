package axle.quanta

import axle.algebra.Bijection
import axle.algebra.DirectedGraph
import spire.algebra.Eq
import spire.algebra.Field

case class Area[N]() extends Quantum4[N] {

  type Q = Area[N]

  def wikipediaUrl: String = "http://en.wikipedia.org/wiki/Area"

  def unit(name: String, symbol: String, wiki: Option[String] = None) =
    UnitOfMeasurement4[Q, N](name, symbol, wiki)

  def m2: UnitOfMeasurement4[Q, N] = unit("m2", "m2") // derive
  def km2: UnitOfMeasurement4[Q, N] = unit("km2", "km2") // derive
  def cm2: UnitOfMeasurement4[Q, N] = unit("cm2", "cm2") // derive

  def units: List[UnitOfMeasurement4[Q, N]] =
    List(m2, km2, cm2)

  def links(implicit fn: Field[N]): Seq[(UnitOfMeasurement4[Q, N], UnitOfMeasurement4[Q, N], Bijection[N, N])] =
    List[(UnitOfMeasurement4[Q, N], UnitOfMeasurement4[Q, N], Bijection[N, N])](
      (m2, km2, Scale10s(6)),
      (cm2, m2, Scale10s(6)))

}
