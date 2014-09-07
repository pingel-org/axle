package axle.quanta

import axle.graph.DirectedGraph
import axle.graph.Vertex
import spire.algebra.Eq
import spire.algebra.Field
import spire.implicits.DoubleAlgebra
import spire.math.Rational
import spire.math.Real
import spire.implicits._

abstract class Flow extends Quantum {
  def wikipediaUrl = "http://en.wikipedia.org/wiki/Volumetric_flow_rate"
}

object Flow extends Flow {

  type Q = Flow

  import Time._

  def units[N: Field: Eq] = List[UnitOfMeasurement[Q, N]](
    unit("m3s", "m3s"), // derive
    unit("Niagara Falls Flow", "Niagara Falls Flow", Some("http://en.wikipedia.org/wiki/Niagara_Falls")))

  def links[N: Field: Eq] = {
    implicit val baseCG = cgnDisconnected[N]
    List[(UnitOfMeasurement[Q, N], UnitOfMeasurement[Q, N], N => N, N => N)](
      (m3s, niagaraFalls, _ * 1834, _ / 1834))
  }

  //  implicit val cgFlowRational: DirectedGraph[UnitOfMeasurement[Flow, Rational], Rational => Rational] = cgn[Rational]
  //  implicit val mtRational = modulize[Flow, Rational]

  def niagaraFalls[N](implicit fieldN: Field[N], eqN: Eq[N], cg: DirectedGraph[UnitOfMeasurement[Flow, N], N => N]) = byName(cg, "Niagara Falls Flow")
  def m3s[N](implicit fieldN: Field[N], eqN: Eq[N], cg: DirectedGraph[UnitOfMeasurement[Flow, N], N => N]) = byName(cg, "m3s")

}
