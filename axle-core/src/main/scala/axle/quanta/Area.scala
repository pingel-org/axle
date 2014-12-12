package axle.quanta

import axle.algebra.Bijection
import axle.algebra.DirectedGraph
import spire.algebra.Eq
import spire.algebra.Field

case class Area() extends Quantum("http://en.wikipedia.org/wiki/Area")

object Area {

  type Q = Area

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

  implicit def conversionGraph[N: Field: Eq, DG[_, _]: DirectedGraph] =
    Quantum.cgn(units[N], links[N])

}

