package axle.quanta

import axle.algebra.Vertex
import axle.algebra.Bijection
import axle.algebra.DirectedGraph
import spire.algebra.Eq
import spire.algebra.Field
import spire.math.Rational
import spire.math.Real

case class Flow3() extends Quantum

object Flow3 extends Quantum {

  def wikipediaUrl = "http://en.wikipedia.org/wiki/Volumetric_flow_rate"

  def unit[N: Field: Eq](name: String, symbol: String, wiki: Option[String] = None) =
    UnitOfMeasurement[Flow3, N](name, symbol, wiki)

  def m3s[N: Field: Eq] = unit("m3s", "m3s") // derive
  def niagaraFalls[N: Field: Eq] = unit("Niagara Falls Flow", "Niagara Falls Flow", Some("http://en.wikipedia.org/wiki/Niagara_Falls"))

  def units[N: Field: Eq]: List[UnitOfMeasurement[Flow3, N]] =
    List(m3s, niagaraFalls)

  def links[N: Field: Eq]: Seq[(UnitOfMeasurement[Flow3, N], UnitOfMeasurement[Flow3, N], Bijection[N, N])] =
    List[(UnitOfMeasurement[Flow3, N], UnitOfMeasurement[Flow3, N], Bijection[N, N])](
      (m3s, niagaraFalls, ScaleInt(1834)))

  implicit def conversionGraph[N: Field: Eq, DG[_, _]: DirectedGraph] =
    Quantum.cgn(units, links)

}