package axle.quanta

import axle.algebra.Bijection
import axle.algebra.DirectedGraph
import spire.algebra.Eq
import spire.algebra.Field

case class Force[N]() extends Quantum4[N] {

  type Q = Force[N]

  def wikipediaUrl: String = "http://en.wikipedia.org/wiki/Force"

  def unit(name: String, symbol: String, wiki: Option[String] = None) =
    UnitOfMeasurement4[Q, N](name, symbol, wiki)

  def pound: UnitOfMeasurement4[Q, N] = unit("pound", "lb", Some("http://en.wikipedia.org/wiki/Pound-force"))
  def newton: UnitOfMeasurement4[Q, N] = unit("newton", "N", Some("http://en.wikipedia.org/wiki/Newton_(unit)"))
  def dyne: UnitOfMeasurement4[Q, N] = unit("dyne", "dyn", Some("http://en.wikipedia.org/wiki/Dyne"))

  def units: List[UnitOfMeasurement4[Q, N]] =
    List(pound, newton, dyne)

  def links(implicit fn: Field[N]): Seq[(UnitOfMeasurement4[Q, N], UnitOfMeasurement4[Q, N], Bijection[N, N])] =
    List.empty

}
