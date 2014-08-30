package axle.quanta2

import axle.graph.DirectedGraph
import axle.graph.Vertex
import spire.algebra.Eq
import spire.algebra.Field
import spire.implicits.DoubleAlgebra
import spire.math.Rational
import spire.math.Real

class Mass extends Quantum {
  def wikipediaUrl = "http://en.wikipedia.org/wiki/Orders_of_magnitude_(mass)"
  // "http://en.wikipedia.org/wiki/Mass"
}

object Mass extends Mass {

  implicit def cgn[N: Field: Eq]: DirectedGraph[Quantity[Mass, N], N => N] = conversions(
    List(
      unit("milligram", "mg"),
      unit("gram", "g"),
      unit("kilogram", "Kg"),
      unit("megagram", "Mg")),
    (vs: Seq[Vertex[Quantity[Mass, N]]]) => vs match {
      case s :: Nil => trips2fns[Mass, N](List())
      case _ => Nil
    })

  implicit val cgMassRational: DirectedGraph[Quantity[Mass, Rational], Rational => Rational] = cgn[Rational]
  implicit val cgMassReal: DirectedGraph[Quantity[Mass, Real], Real => Real] = cgn[Real]
  implicit val cgMassDouble: DirectedGraph[Quantity[Mass, Double], Double => Double] = cgn[Double]

  implicit val mtRational = modulize[Mass, Rational]
  implicit val mtReal = modulize[Mass, Real]
  implicit val mtDouble = modulize[Mass, Double]

  def milligram[N](implicit fieldN: Field[N], eqN: Eq[N], cg: DirectedGraph[Quantity[Mass, N], N => N]) = byName(cg, "milligram")
  def gram[N](implicit fieldN: Field[N], eqN: Eq[N], cg: DirectedGraph[Quantity[Mass, N], N => N]) = byName(cg, "gram")
  def kilogram[N](implicit fieldN: Field[N], eqN: Eq[N], cg: DirectedGraph[Quantity[Mass, N], N => N]) = byName(cg, "kilogram")
  def megagram[N](implicit fieldN: Field[N], eqN: Eq[N], cg: DirectedGraph[Quantity[Mass, N], N => N]) = byName(cg, "megagram")

}
