package axle.quanta

import axle.algebra.Bijection
import axle.algebra.DirectedGraph
import spire.algebra.Eq
import spire.algebra.Field

case class Money() extends Quantum("http://en.wikipedia.org/wiki/Money")

object Money {

  type Q = Money

  def unit[N](name: String, symbol: String, wiki: Option[String] = None) =
    UnitOfMeasurement[Q, N](name, symbol, wiki)

  def USD[N]: UnitOfMeasurement[Q, N] = unit("US Dollar", "USD")

  def units[N]: List[UnitOfMeasurement[Q, N]] =
    List(USD)

  def links[N: Field]: Seq[(UnitOfMeasurement[Q, N], UnitOfMeasurement[Q, N], Bijection[N, N])] =
    List.empty

  implicit def conversionGraph[N: Field: Eq, DG[_, _]: DirectedGraph] =
    Quantum.cgn(units[N], links)

}
