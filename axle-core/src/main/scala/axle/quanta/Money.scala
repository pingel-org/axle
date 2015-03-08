package axle.quanta

import axle.algebra.Bijection
import axle.algebra.DirectedGraph
import spire.algebra.Eq
import spire.algebra.Field

case class Money[N]() extends Quantum[N] {

  type Q = Money[N]

  def wikipediaUrl: String = "http://en.wikipedia.org/wiki/Money"

  def unit(name: String, symbol: String, wiki: Option[String] = None) =
    UnitOfMeasurement[Money[N], N](name, symbol, wiki)

  lazy val USD = unit("US Dollar", "USD")

  def units: List[UnitOfMeasurement[Money[N], N]] =
    List(USD)

  def links(implicit fn: Field[N]): Seq[(UnitOfMeasurement[Money[N], N], UnitOfMeasurement[Money[N], N], Bijection[N, N])] =
    List.empty

}
