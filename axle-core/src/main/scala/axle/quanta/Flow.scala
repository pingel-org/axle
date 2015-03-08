package axle.quanta

import axle.algebra.Bijection
import axle.algebra.DirectedGraph
import spire.algebra.Eq
import spire.algebra.Field
import spire.math.Rational

case class Flow() extends Quantum {

  def wikipediaUrl: String = "http://en.wikipedia.org/wiki/Volumetric_flow_rate"

}

object Flow {

  def metadata[N] = new QuantumMetadata[Flow, N] {

    def unit(name: String, symbol: String, wiki: Option[String] = None) =
      UnitOfMeasurement[Flow, N](name, symbol, wiki)

    lazy val m3s = unit("m3s", "m3s") // derive

    lazy val niagaraFalls = unit("Niagara Falls Flow", "Niagara Falls Flow", Some("http://en.wikipedia.org/wiki/Niagara_Falls"))

    def units: List[UnitOfMeasurement[Flow, N]] =
      List(m3s, niagaraFalls)

    def links(implicit fn: Field[N]): Seq[(UnitOfMeasurement[Flow, N], UnitOfMeasurement[Flow, N], Bijection[N, N])] =
      List[(UnitOfMeasurement[Flow, N], UnitOfMeasurement[Flow, N], Bijection[N, N])](
        (m3s, niagaraFalls, ScaleInt(1834)))

  }
}