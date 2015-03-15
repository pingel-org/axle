package axle.quanta

import axle.algebra.Bijection
import axle.algebra.DirectedGraph
import spire.algebra.Eq
import spire.algebra.Field

case class MoneyPerForce() extends Quantum {

  def wikipediaUrl: String = "http://en.wikipedia.org/wiki/Degree_(MoneyPerForce)"

}

trait MoneyPerForceUnits extends QuantumUnits[MoneyPerForce] {

  lazy val USDperPound = unit("$/lb", "$/lb") // derive

  def units: List[UnitOfMeasurement[MoneyPerForce]] =
    List(USDperPound)

}

trait MoneyPerForceMetadata[N] extends QuantumMetadata[MoneyPerForce, N] with MoneyPerForceUnits

object MoneyPerForce {

  def metadata[N: Field: Eq, DG[_, _]: DirectedGraph] =
    new QuantumMetadataGraph[MoneyPerForce, N, DG] with MoneyPerForceMetadata[N] {

      def links: Seq[(UnitOfMeasurement[MoneyPerForce], UnitOfMeasurement[MoneyPerForce], Bijection[N, N])] =
        List.empty

    }

}