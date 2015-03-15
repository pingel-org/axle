package axle.quanta

import axle.algebra.Bijection
import axle.algebra.DirectedGraph
import spire.algebra.Eq
import spire.algebra.Field

case class MoneyFlow() extends Quantum {

  def wikipediaUrl: String = ""

}

trait MoneyFlowUnits[N] {

  type U = UnitOfMeasurement[MoneyFlow, N]

  def USDperHour: U

}

trait MoneyFlowMetadata[N] extends QuantumMetadata[MoneyFlow, N] with MoneyFlowUnits[N]

object MoneyFlow {

  def metadata[N: Field: Eq, DG[_, _]: DirectedGraph] =
    new QuantumMetadataGraph[MoneyFlow, N, DG] with MoneyFlowMetadata[N] {

      def unit(name: String, symbol: String, wiki: Option[String] = None) =
        UnitOfMeasurement[MoneyFlow, N](name, symbol, wiki)

      lazy val _USDperHour = unit("$/hr", "$/hr") // derive

      def USDperHour = _USDperHour

      def units: List[UnitOfMeasurement[MoneyFlow, N]] =
        List(USDperHour)

      def links: Seq[(UnitOfMeasurement[MoneyFlow, N], UnitOfMeasurement[MoneyFlow, N], Bijection[N, N])] =
        List.empty

    }

}