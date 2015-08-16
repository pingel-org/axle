package axle.quanta

import axle.algebra.Bijection
import axle.algebra.DirectedGraph
import axle.algebra.Scale10s
import spire.algebra.Eq
import spire.algebra.Field

case class Area() extends Quantum {

  def wikipediaUrl: String = "http://en.wikipedia.org/wiki/Area"

}

trait AreaUnits extends QuantumUnits[Area] {

  lazy val m2 = unit("m2", "m2") // derive
  lazy val km2 = unit("km2", "km2") // derive
  lazy val cm2 = unit("cm2", "cm2") // derive

  def units: List[UnitOfMeasurement[Area]] =
    List(m2, km2, cm2)

}

trait AreaConverter[N] extends UnitConverter[Area, N] with AreaUnits {

  def defaultUnit = m2
}

object Area {

  def converterGraphK2[N: Field: Eq, DG[_, _]](
    implicit evDG: DirectedGraph[DG[UnitOfMeasurement[Area], N => N], UnitOfMeasurement[Area], N => N]) =
    converterGraph[N, DG[UnitOfMeasurement[Area], N => N]]

  def converterGraph[N: Field: Eq, DG](
    implicit evDG: DirectedGraph[DG, UnitOfMeasurement[Area], N => N]) =
    new UnitConverterGraph[Area, N, DG] with AreaConverter[N] {

      def links: Seq[(UnitOfMeasurement[Area], UnitOfMeasurement[Area], Bijection[N, N])] =
        List[(UnitOfMeasurement[Area], UnitOfMeasurement[Area], Bijection[N, N])](
          (m2, km2, Scale10s(6)),
          (cm2, m2, Scale10s(6)))

    }

}