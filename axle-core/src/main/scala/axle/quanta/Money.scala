package axle.quanta

import axle.algebra.Bijection
import axle.algebra.DirectedGraph
import cats.kernel.Eq
import spire.algebra.Field

case class Money() extends Quantum {

  def wikipediaUrl: String = "http://en.wikipedia.org/wiki/Money"

}

trait MoneyUnits extends QuantumUnits[Money] {

  lazy val USD = unit("US Dollar", "USD")

  def units: List[UnitOfMeasurement[Money]] =
    List(USD)

}

trait MoneyConverter[N] extends UnitConverter[Money, N] with MoneyUnits {

  def defaultUnit = USD
}

object Money {

  def converterGraphK2[N: Field: Eq, DG[_, _]](
    implicit evDG: DirectedGraph[DG[UnitOfMeasurement[Money], N => N], UnitOfMeasurement[Money], N => N]) =
    converterGraph[N, DG[UnitOfMeasurement[Money], N => N]]

  def converterGraph[N: Field: Eq, DG](
    implicit evDG: DirectedGraph[DG, UnitOfMeasurement[Money], N => N]) =
    new UnitConverterGraph[Money, N, DG] with MoneyConverter[N] {

      def links: Seq[(UnitOfMeasurement[Money], UnitOfMeasurement[Money], Bijection[N, N])] =
        List.empty

    }

}
