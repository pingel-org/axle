package axle.quanta

import axle.algebra.Vertex
import axle.algebra.Bijection
import axle.algebra.DirectedGraph
import spire.algebra.Eq
import spire.algebra.Field
import spire.math.Rational
import spire.math.Real

class Flow[DG[_, _]: DirectedGraph] extends Quantum {

  def wikipediaUrl = "http://en.wikipedia.org/wiki/Volumetric_flow_rate"

  type Q = this.type

  // import Time._

  def units[N: Field: Eq] = List[UnitOfMeasurement[Q, N]](
    unit("m3s", "m3s"), // derive
    unit("Niagara Falls Flow", "Niagara Falls Flow", Some("http://en.wikipedia.org/wiki/Niagara_Falls")))

  def links[N: Field: Eq] = {
    implicit val baseCG = cgnDisconnected[N, DG]
    List[(UnitOfMeasurement[Q, N], UnitOfMeasurement[Q, N], Bijection[N, N])](
      (m3s, niagaraFalls, ScaleInt(1834)))
  }

  //  implicit val cgFlowRational: DirectedGraph[UnitOfMeasurement[Flow, Rational], Rational => Rational] = cgn[Rational]
  //  implicit val mtRational = modulize[Flow, Rational]

  def niagaraFalls[N](implicit fieldN: Field[N], eqN: Eq[N], cg: CG[DG, N]) =
    byName(cg, "Niagara Falls Flow")

  def m3s[N](implicit fieldN: Field[N], eqN: Eq[N], cg: CG[DG, N]) =
    byName(cg, "m3s")

}

case class Flow3() extends Quantum3

object Flow3 extends Quantum3 {

  def unit[N: Field: Eq](name: String, symbol: String, wiki: Option[String] = None) =
    UnitOfMeasurement3[Flow3, N](name, symbol, wiki)

  def m3s[N: Field: Eq] = unit("m3s", "m3s") // derive
  def niagaraFalls[N: Field: Eq] = unit("Niagara Falls Flow", "Niagara Falls Flow", Some("http://en.wikipedia.org/wiki/Niagara_Falls"))

  def units[N: Field: Eq]: List[UnitOfMeasurement3[Flow3, N]] =
    List(m3s, niagaraFalls)

  def links[N: Field: Eq]: Seq[(UnitOfMeasurement3[Flow3, N], UnitOfMeasurement3[Flow3, N], Bijection[N, N])] =
    List[(UnitOfMeasurement3[Flow3, N], UnitOfMeasurement3[Flow3, N], Bijection[N, N])](
      (m3s, niagaraFalls, ScaleInt(1834)))

  implicit def conversionGraph[N: Field: Eq, DG[_, _]: DirectedGraph] =
    Quantum3.cgn(units, links)

}