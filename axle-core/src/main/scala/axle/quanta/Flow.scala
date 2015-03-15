package axle.quanta

import axle.algebra.Bijection
import axle.algebra.DirectedGraph
import spire.algebra.Eq
import spire.algebra.Field
import spire.math.Rational

case class Flow() extends Quantum {

  def wikipediaUrl: String = "http://en.wikipedia.org/wiki/Volumetric_flow_rate"

}

abstract class FlowMetadata[N: Field: Eq, DG[_, _]: DirectedGraph]
  extends QuantumMetadataGraph[Flow, N, DG] {

  type U = UnitOfMeasurement[Flow, N]

  def m3s: U
  def niagaraFalls: U

}

object Flow {

  def metadata[N: Field: Eq, DG[_, _]: DirectedGraph] = new FlowMetadata[N, DG] {

    def unit(name: String, symbol: String, wiki: Option[String] = None) =
      UnitOfMeasurement[Flow, N](name, symbol, wiki)

    lazy val _m3s = unit("m3s", "m3s") // derive

    lazy val _niagaraFalls = unit("Niagara Falls Flow", "Niagara Falls Flow", Some("http://en.wikipedia.org/wiki/Niagara_Falls"))

    def m3s = _m3s
    def niagaraFalls = _niagaraFalls

    def units: List[UnitOfMeasurement[Flow, N]] =
      List(m3s, niagaraFalls)

    def links: Seq[(UnitOfMeasurement[Flow, N], UnitOfMeasurement[Flow, N], Bijection[N, N])] =
      List[(UnitOfMeasurement[Flow, N], UnitOfMeasurement[Flow, N], Bijection[N, N])](
        (m3s, niagaraFalls, ScaleInt(1834)))

  }
}