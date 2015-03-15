package axle.quanta

import axle.algebra.Bijection
import axle.algebra.DirectedGraph
import spire.algebra.Eq
import spire.algebra.Field

case class MoneyFlow() extends Quantum {

  def wikipediaUrl: String = ""

}

trait MoneyFlowUnits {

  def unit(name: String, symbol: String, wiki: Option[String] = None) =
    UnitOfMeasurement[MoneyFlow](name, symbol, wiki)

  lazy val USDperHour = unit("$/hr", "$/hr") // derive

}

trait MoneyFlowMetadata[N] extends QuantumMetadata[MoneyFlow, N] with MoneyFlowUnits

object MoneyFlow {

  def metadata[N: Field: Eq, DG[_, _]: DirectedGraph] =
    new QuantumMetadataGraph[MoneyFlow, N, DG] with MoneyFlowMetadata[N] {

      def units: List[UnitOfMeasurement[MoneyFlow]] =
        List(USDperHour)

      def links: Seq[(UnitOfMeasurement[MoneyFlow], UnitOfMeasurement[MoneyFlow], Bijection[N, N])] =
        List.empty

    }

}