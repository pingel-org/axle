package axle.quanta

import axle.algebra.Bijection
import axle.algebra.DirectedGraph
import spire.algebra.Eq
import spire.algebra.Field

case object Money extends Quantum {

  type Q = Money.type

  def wikipediaUrl: String = "http://en.wikipedia.org/wiki/Money"

  def unit[N](name: String, symbol: String, wiki: Option[String] = None) =
    UnitOfMeasurement[Q, N](name, symbol, wiki)

  def USD[N]: UnitOfMeasurement[Q, N] = unit("US Dollar", "USD")

  def units[N]: List[UnitOfMeasurement[Q, N]] =
    List(USD)

  def links[N: Field]: Seq[(UnitOfMeasurement[Q, N], UnitOfMeasurement[Q, N], Bijection[N, N])] =
    List.empty

}
