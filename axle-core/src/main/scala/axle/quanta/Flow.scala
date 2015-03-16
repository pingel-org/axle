package axle.quanta

import axle.algebra.Bijection
import axle.algebra.DirectedGraph
import spire.algebra.Eq
import spire.algebra.Field
import spire.math.Rational

case class Flow() extends Quantum {

  def wikipediaUrl: String = "http://en.wikipedia.org/wiki/Volumetric_flow_rate"

}

trait FlowUnits extends QuantumUnits[Flow] {

  lazy val m3s = unit("m3s", "m3s") // derive

  lazy val niagaraFalls = unit("Niagara Falls Flow", "Niagara Falls Flow", Some("http://en.wikipedia.org/wiki/Niagara_Falls"))

  def units: List[UnitOfMeasurement[Flow]] =
    List(m3s, niagaraFalls)

}

trait FlowConverter[N] extends UnitConverter[Flow, N] with FlowUnits

object Flow {

  def converterGraph[N: Field: Eq, DG[_, _]: DirectedGraph] =
    new UnitConverterGraph[Flow, N, DG] with FlowConverter[N] {

      def links: Seq[(UnitOfMeasurement[Flow], UnitOfMeasurement[Flow], Bijection[N, N])] =
        List[(UnitOfMeasurement[Flow], UnitOfMeasurement[Flow], Bijection[N, N])](
          (m3s, niagaraFalls, ScaleInt(1834)))

    }
}