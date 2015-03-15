package axle.quanta

import axle.algebra.Bijection
import axle.algebra.DirectedGraph
import spire.algebra.Eq
import spire.algebra.Field

case class MoneyPerForce() extends Quantum {

  def wikipediaUrl: String = "http://en.wikipedia.org/wiki/Degree_(MoneyPerForce)"

}

trait MoneyPerForceUnits {

  def unit(name: String, symbol: String, wiki: Option[String] = None) =
    UnitOfMeasurement[MoneyPerForce](name, symbol, wiki)

  lazy val USDperPound = unit("$/lb", "$/lb") // derive
}

trait MoneyPerForceMetadata[N] extends QuantumMetadata[MoneyPerForce, N] with MoneyPerForceUnits

object MoneyPerForce {

  def metadata[N: Field: Eq, DG[_, _]: DirectedGraph] =
    new QuantumMetadataGraph[MoneyPerForce, N, DG] with MoneyPerForceMetadata[N] {

      def units: List[UnitOfMeasurement[MoneyPerForce]] =
        List(USDperPound)

      def links: Seq[(UnitOfMeasurement[MoneyPerForce], UnitOfMeasurement[MoneyPerForce], Bijection[N, N])] =
        List.empty

    }

}