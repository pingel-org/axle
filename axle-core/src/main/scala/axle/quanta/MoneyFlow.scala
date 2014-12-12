package axle.quanta

import axle.algebra.Bijection
import axle.algebra.DirectedGraph
import spire.algebra.Eq
import spire.algebra.Field

case object MoneyFlow extends Quantum {

  type Q = MoneyFlow.type

  def wikipediaUrl: String = ""

  def unit[N](name: String, symbol: String, wiki: Option[String] = None) =
    UnitOfMeasurement[Q, N](name, symbol, wiki)

  def USDperHour[N]: UnitOfMeasurement[Q, N] = unit("$/hr", "$/hr") // derive

  def units[N]: List[UnitOfMeasurement[Q, N]] =
    List(USDperHour)

  def links[N: Field]: Seq[(UnitOfMeasurement[Q, N], UnitOfMeasurement[Q, N], Bijection[N, N])] =
    List.empty

}
