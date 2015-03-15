package axle.quanta

import axle.algebra.Bijection
import axle.algebra.DirectedGraph
import spire.algebra.Eq
import spire.algebra.Field

case class Money() extends Quantum {

  def wikipediaUrl: String = "http://en.wikipedia.org/wiki/Money"

}

trait MoneyUnits {

  type U = UnitOfMeasurement[Money]

  def USD: U

}

trait MoneyMetadata[N] extends QuantumMetadata[Money, N] with MoneyUnits

object Money {

  def metadata[N: Field: Eq, DG[_, _]: DirectedGraph] =
    new QuantumMetadataGraph[Money, N, DG] with MoneyMetadata[N] {

      def unit(name: String, symbol: String, wiki: Option[String] = None) =
        UnitOfMeasurement[Money](name, symbol, wiki)

      lazy val _USD = unit("US Dollar", "USD")

      def USD = _USD

      def units: List[UnitOfMeasurement[Money]] =
        List(USD)

      def links: Seq[(UnitOfMeasurement[Money], UnitOfMeasurement[Money], Bijection[N, N])] =
        List.empty

    }

}
