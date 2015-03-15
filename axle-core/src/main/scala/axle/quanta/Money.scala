package axle.quanta

import axle.algebra.Bijection
import axle.algebra.DirectedGraph
import spire.algebra.Eq
import spire.algebra.Field

case class Money() extends Quantum {

  def wikipediaUrl: String = "http://en.wikipedia.org/wiki/Money"

}

trait MoneyUnits extends QuantumUnits[Money] {

  lazy val USD = unit("US Dollar", "USD")

  def units: List[UnitOfMeasurement[Money]] =
    List(USD)

}

trait MoneyMetadata[N] extends QuantumMetadata[Money, N] with MoneyUnits

object Money {

  def metadata[N: Field: Eq, DG[_, _]: DirectedGraph] =
    new QuantumMetadataGraph[Money, N, DG] with MoneyMetadata[N] {

      def links: Seq[(UnitOfMeasurement[Money], UnitOfMeasurement[Money], Bijection[N, N])] =
        List.empty

    }

}
