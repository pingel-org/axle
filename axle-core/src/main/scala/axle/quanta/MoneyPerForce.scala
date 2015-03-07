package axle.quanta

import axle.algebra.Bijection
import axle.algebra.DirectedGraph
import spire.algebra.Eq
import spire.algebra.Field

case class MoneyPerForce[N]() extends Quantum4[N] {

  type Q = MoneyPerForce[N]

  def wikipediaUrl: String = "http://en.wikipedia.org/wiki/Degree_(MoneyPerForce)"

  def unit(name: String, symbol: String, wiki: Option[String] = None) =
    UnitOfMeasurement4[Q, N](name, symbol, wiki)

  def USDperPound: UnitOfMeasurement4[Q, N] = unit("$/lb", "$/lb") // derive

  def units: List[UnitOfMeasurement4[Q, N]] =
    List(USDperPound)

  def links(implicit fn: Field[N]): Seq[(UnitOfMeasurement4[Q, N], UnitOfMeasurement4[Q, N], Bijection[N, N])] =
    List.empty

}
