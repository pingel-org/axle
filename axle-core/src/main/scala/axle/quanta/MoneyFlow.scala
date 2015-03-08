package axle.quanta

import axle.algebra.Bijection
import axle.algebra.DirectedGraph
import spire.algebra.Eq
import spire.algebra.Field

case class MoneyFlow() extends Quantum {

  def wikipediaUrl: String = ""

}

object MoneyFlow {

  def metadata[N] = new QuantumMetadata[MoneyFlow, N] {

    def unit(name: String, symbol: String, wiki: Option[String] = None) =
      UnitOfMeasurement[MoneyFlow, N](name, symbol, wiki)

    lazy val USDperHour = unit("$/hr", "$/hr") // derive

    def units: List[UnitOfMeasurement[MoneyFlow, N]] =
      List(USDperHour)

    def links(implicit fn: Field[N]): Seq[(UnitOfMeasurement[MoneyFlow, N], UnitOfMeasurement[MoneyFlow, N], Bijection[N, N])] =
      List.empty

  }

}