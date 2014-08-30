package axle.quanta2

import axle.graph.DirectedGraph
import axle.graph.Vertex
import spire.algebra.Eq
import spire.algebra.Field
import spire.implicits.DoubleAlgebra
import spire.math.Rational
import spire.math.Real

class Time extends Quantum {
  def wikipediaUrl = "http://en.wikipedia.org/wiki/Orders_of_magnitude_(time)"
}

object Time extends Time {

  implicit def cgn[N: Field: Eq]: DirectedGraph[Quantity[Time, N], N => N] = conversions(
    List(
      unit("second", "s"),
      unit("minute", "min")),
    (vs: Seq[Vertex[Quantity[Time, N]]]) => vs match {
      case s :: Nil => trips2fns[Time, N](List())
      case _ => Nil
    })

  implicit val cgTimeRational: DirectedGraph[Quantity[Time, Rational], Rational => Rational] = cgn[Rational]
  implicit val cgTimeReal: DirectedGraph[Quantity[Time, Real], Real => Real] = cgn[Real]
  implicit val cgTimeDouble: DirectedGraph[Quantity[Time, Double], Double => Double] = cgn[Double]

  implicit val mtRational = modulize[Time, Rational]
  implicit val mtReal = modulize[Time, Real]
  implicit val mtDouble = modulize[Time, Double]

  def second[N](implicit fieldN: Field[N], eqN: Eq[N], cg: DirectedGraph[Quantity[Time, N], N => N]) = byName(cg, "second")
  def minute[N](implicit fieldN: Field[N], eqN: Eq[N], cg: DirectedGraph[Quantity[Time, N], N => N]) = byName(cg, "minute")

}
