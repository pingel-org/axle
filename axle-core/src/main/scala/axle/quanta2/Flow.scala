package axle.quanta2

import axle.graph.DirectedGraph
import axle.graph.Vertex
import spire.algebra.Eq
import spire.algebra.Field
import spire.implicits.DoubleAlgebra
import spire.math.Rational
import spire.math.Real

class Flow extends Quantum {
  def wikipediaUrl = "http://en.wikipedia.org/wiki/Flowtric_flow_rate"
}

object Flow extends Flow {

  def cgn[N: Field: Eq]: DirectedGraph[UnitOfMeasurement[Flow, N], N => N] = conversions(
    List(
      unit("niagaraFalls", "niagaraFalls")), // 5 bottles of wine
    (vs: Seq[Vertex[UnitOfMeasurement[Flow, N]]]) => vs match {
      case niagaraFalls :: Nil => List(
          // TODO
          )
      case _ => Nil
    })

  implicit val cgFlowRational: DirectedGraph[UnitOfMeasurement[Flow, Rational], Rational => Rational] = cgn[Rational]
  implicit val cgFlowReal: DirectedGraph[UnitOfMeasurement[Flow, Real], Real => Real] = cgn[Real]
  implicit val cgFlowDouble: DirectedGraph[UnitOfMeasurement[Flow, Double], Double => Double] = cgn[Double]

  implicit val mtRational = modulize[Flow, Rational]
  implicit val mtReal = modulize[Flow, Real]
  implicit val mtDouble = modulize[Flow, Double]

  def niagaraFalls[N](implicit fieldN: Field[N], eqN: Eq[N], cg: DirectedGraph[UnitOfMeasurement[Flow, N], N => N]) = byName(cg, "niagaraFalls")
  
  
}
