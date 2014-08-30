package axle.quanta2

import axle.graph.DirectedGraph
import axle.graph.Vertex
import spire.algebra.Eq
import spire.algebra.Field
import spire.implicits.DoubleAlgebra
import spire.math.Rational
import spire.math.Real

class Angle extends Quantum {
  def wikipediaUrl = "http://en.wikipedia.org/wiki/Degree_(angle)"
}

object Angle extends Angle {

  implicit def cgn[N: Field: Eq]: DirectedGraph[Quantity[Angle, N], N => N] = conversions(
    List(
      unit("degree", "°"),
      unit("radian", "rad")),
    (vs: Seq[Vertex[Quantity[Angle, N]]]) => vs match {
      case s :: Nil => trips2fns[Angle, N](List())
      case _ => Nil
    })

  implicit val cgAngleRational: DirectedGraph[Quantity[Angle, Rational], Rational => Rational] = cgn[Rational]
  implicit val cgAngleReal: DirectedGraph[Quantity[Angle, Real], Real => Real] = cgn[Real]
  implicit val cgAngleDouble: DirectedGraph[Quantity[Angle, Double], Double => Double] = cgn[Double]

  implicit val mtRational = modulize[Angle, Rational]
  implicit val mtReal = modulize[Angle, Real]
  implicit val mtDouble = modulize[Angle, Double]

  def degree[N](implicit fieldN: Field[N], eqN: Eq[N], cg: DirectedGraph[Quantity[Angle, N], N => N]) = byName(cg, "degree")
  def °[N](implicit fieldN: Field[N], eqN: Eq[N], cg: DirectedGraph[Quantity[Angle, N], N => N]) = byName(cg, "degree")
  def radian[N](implicit fieldN: Field[N], eqN: Eq[N], cg: DirectedGraph[Quantity[Angle, N], N => N]) = byName(cg, "radian")
  def rad[N](implicit fieldN: Field[N], eqN: Eq[N], cg: DirectedGraph[Quantity[Angle, N], N => N]) = byName(cg, "radian")
  
}
