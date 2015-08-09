package axle.quanta

import axle.algebra.Bijection
import axle.algebra.DirectedGraph
import spire.algebra.Eq
import spire.algebra.Field

case class Flow() extends Quantum {

  def wikipediaUrl: String = "http://en.wikipedia.org/wiki/Volumetric_flow_rate"

}

trait FlowUnits extends QuantumUnits[Flow] {

  lazy val m3s = unit("m3s", "m3s") // derive

  def units: List[UnitOfMeasurement[Flow]] =
    List(m3s)

}

trait FlowConverter[N] extends UnitConverter[Flow, N] with FlowUnits {

  def defaultUnit = m3s
}

object Flow {

  def converterGraph[N: Field: Eq, DG](implicit evDG: DirectedGraph[DG, UnitOfMeasurement[Flow], N => N]) =
    new UnitConverterGraph[Flow, N, DG] with FlowConverter[N] {

      def links: Seq[(UnitOfMeasurement[Flow], UnitOfMeasurement[Flow], Bijection[N, N])] =
        List[(UnitOfMeasurement[Flow], UnitOfMeasurement[Flow], Bijection[N, N])]()

    }
}