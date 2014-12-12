package axle.quanta

import axle.algebra.Bijection
import axle.algebra.DirectedGraph
import spire.algebra.Eq
import spire.algebra.Field

case object Flow extends Quantum {

  type Q = Flow.type

  def wikipediaUrl: String = "http://en.wikipedia.org/wiki/Volumetric_flow_rate"
  
  def unit[N](name: String, symbol: String, wiki: Option[String] = None) =
    UnitOfMeasurement[Q, N](name, symbol, wiki)

  def m3s[N]: UnitOfMeasurement[Q, N] = unit("m3s", "m3s") // derive
  def niagaraFalls[N]: UnitOfMeasurement[Q, N] = unit("Niagara Falls Flow", "Niagara Falls Flow", Some("http://en.wikipedia.org/wiki/Niagara_Falls"))

  def units[N]: List[UnitOfMeasurement[Q, N]] =
    List(m3s, niagaraFalls)

  def links[N: Field]: Seq[(UnitOfMeasurement[Q, N], UnitOfMeasurement[Q, N], Bijection[N, N])] =
    List[(UnitOfMeasurement[Q, N], UnitOfMeasurement[Q, N], Bijection[N, N])](
      (m3s, niagaraFalls, ScaleInt(1834)))

}