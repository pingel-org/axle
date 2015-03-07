package axle.quanta

import axle.algebra.Bijection
import axle.algebra.DirectedGraph
import spire.algebra.Eq
import spire.algebra.Field

case class MoneyFlow[N]() extends Quantum4[N] {

  type Q = MoneyFlow[N]

  def wikipediaUrl: String = ""

  def unit(name: String, symbol: String, wiki: Option[String] = None) =
    UnitOfMeasurement4[MoneyFlow[N], N](name, symbol, wiki)

  lazy val USDperHour = unit("$/hr", "$/hr") // derive

  def units: List[UnitOfMeasurement4[MoneyFlow[N], N]] =
    List(USDperHour)

  def links(implicit fn: Field[N]): Seq[(UnitOfMeasurement4[MoneyFlow[N], N], UnitOfMeasurement4[MoneyFlow[N], N], Bijection[N, N])] =
    List.empty

}
