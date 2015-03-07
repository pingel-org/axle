package axle.quanta

import axle.algebra.Bijection
import axle.algebra.DirectedGraph
import spire.algebra.Eq
import spire.algebra.Field

case class Flow[N]() extends Quantum4[N] {

  type Q = Flow[N]

  def wikipediaUrl: String = "http://en.wikipedia.org/wiki/Volumetric_flow_rate"

  def unit(name: String, symbol: String, wiki: Option[String] = None) =
    UnitOfMeasurement4[Q, N](name, symbol, wiki)

  def m3s: UnitOfMeasurement4[Q, N] = unit("m3s", "m3s") // derive
  def niagaraFalls: UnitOfMeasurement4[Q, N] = unit("Niagara Falls Flow", "Niagara Falls Flow", Some("http://en.wikipedia.org/wiki/Niagara_Falls"))

  def units: List[UnitOfMeasurement4[Q, N]] =
    List(m3s, niagaraFalls)

  def links(implicit fn: Field[N]): Seq[(UnitOfMeasurement4[Q, N], UnitOfMeasurement4[Q, N], Bijection[N, N])] =
    List[(UnitOfMeasurement4[Q, N], UnitOfMeasurement4[Q, N], Bijection[N, N])](
      (m3s, niagaraFalls, ScaleInt(1834)))

}