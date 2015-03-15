package axle.quanta

import axle.algebra.Bijection
import axle.algebra.DirectedGraph
import spire.algebra.Eq
import spire.algebra.Field

case class Money() extends Quantum {

  def wikipediaUrl: String = "http://en.wikipedia.org/wiki/Money"

}

trait MoneyUnits[N] {

  type U = UnitOfMeasurement[Money, N]

  def USD: U

}

trait MoneyMetadata[N] extends QuantumMetadata[Money, N] with MoneyUnits[N]

object Money {

  def metadata[N: Field: Eq, DG[_, _]: DirectedGraph] =
    new QuantumMetadataGraph[Money, N, DG] with MoneyMetadata[N] {

      def unit(name: String, symbol: String, wiki: Option[String] = None) =
        UnitOfMeasurement[Money, N](name, symbol, wiki)

      lazy val _USD = unit("US Dollar", "USD")

      def USD = _USD

      def units: List[UnitOfMeasurement[Money, N]] =
        List(USD)

      def links: Seq[(UnitOfMeasurement[Money, N], UnitOfMeasurement[Money, N], Bijection[N, N])] =
        List.empty

    }

}
