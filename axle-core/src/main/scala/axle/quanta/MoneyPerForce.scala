package axle.quanta

import axle.algebra.Bijection
import axle.algebra.DirectedGraph
import spire.algebra.Eq
import spire.algebra.Field

case class MoneyPerForce[N]() extends Quantum4[N] {

  type Q = MoneyPerForce[N]

  def wikipediaUrl: String = "http://en.wikipedia.org/wiki/Degree_(MoneyPerForce)"

  def unit(name: String, symbol: String, wiki: Option[String] = None) =
    UnitOfMeasurement4[MoneyPerForce[N], N](name, symbol, wiki)

  lazy val USDperPound = unit("$/lb", "$/lb") // derive

  def units: List[UnitOfMeasurement4[MoneyPerForce[N], N]] =
    List(USDperPound)

  def links(implicit fn: Field[N]): Seq[(UnitOfMeasurement4[MoneyPerForce[N], N], UnitOfMeasurement4[MoneyPerForce[N], N], Bijection[N, N])] =
    List.empty

}
