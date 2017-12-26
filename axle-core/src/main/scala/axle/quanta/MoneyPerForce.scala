package axle.quanta

import axle.algebra.Bijection
import axle.algebra.DirectedGraph
import cats.kernel.Eq
import spire.algebra.Field

case class MoneyPerForce() extends Quantum {

  def wikipediaUrl: String = "http://en.wikipedia.org/wiki/Degree_(MoneyPerForce)"

}

trait MoneyPerForceUnits extends QuantumUnits[MoneyPerForce] {

  lazy val USDperPound = unit("$/lb", "$/lb") // derive

  def units: List[UnitOfMeasurement[MoneyPerForce]] =
    List(USDperPound)

}

trait MoneyPerForceConverter[N] extends UnitConverter[MoneyPerForce, N] with MoneyPerForceUnits {

  def defaultUnit = USDperPound

}

object MoneyPerForce {

  def converterGraphK2[N: Field: Eq, DG[_, _]](
    implicit
    evDG: DirectedGraph[DG[UnitOfMeasurement[MoneyPerForce], N => N], UnitOfMeasurement[MoneyPerForce], N => N]) =
    converterGraph[N, DG[UnitOfMeasurement[MoneyPerForce], N => N]]

  def converterGraph[N: Field: Eq, DG](
    implicit
    evDG: DirectedGraph[DG, UnitOfMeasurement[MoneyPerForce], N => N]) =
    new UnitConverterGraph[MoneyPerForce, N, DG] with MoneyPerForceConverter[N] {

      def links: Seq[(UnitOfMeasurement[MoneyPerForce], UnitOfMeasurement[MoneyPerForce], Bijection[N, N])] =
        List.empty

    }

}
