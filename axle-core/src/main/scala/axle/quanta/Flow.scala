package axle.quanta

import axle.graph.DirectedGraph
import axle.graph.Vertex
import spire.algebra.Eq
import spire.algebra.Field
import spire.implicits.DoubleAlgebra
import spire.math.Rational
import spire.math.Real

abstract class Flow extends Quantum {
  def wikipediaUrl = "http://en.wikipedia.org/wiki/Flowtric_flow_rate"
}

object Flow extends Flow {

  type Q = Flow

  def units[N: Field: Eq] = List[UnitOfMeasurement[Q, N]](
    unit("niagaraFalls", "niagaraFalls"))

  def links[N: Field: Eq] = List.empty[(UnitOfMeasurement[Q, N], UnitOfMeasurement[Q, N], N => N, N => N)]

//  implicit val cgFlowRational: DirectedGraph[UnitOfMeasurement[Flow, Rational], Rational => Rational] = cgn[Rational]
//  implicit val mtRational = modulize[Flow, Rational]

  def niagaraFalls[N](implicit fieldN: Field[N], eqN: Eq[N], cg: DirectedGraph[UnitOfMeasurement[Flow, N], N => N]) = byName(cg, "niagaraFalls")

}
