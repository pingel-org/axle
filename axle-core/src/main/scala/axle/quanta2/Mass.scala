package axle.quanta2

import axle.graph.DirectedGraph
import axle.graph.Vertex
import spire.algebra.Eq
import spire.algebra.Field
import spire.math.Rational
import spire.math.Real
import spire.implicits._

class Mass extends Quantum {
  def wikipediaUrl = "http://en.wikipedia.org/wiki/Orders_of_magnitude_(mass)"
  // "http://en.wikipedia.org/wiki/Mass"
}

object Mass extends Mass {

  import spire.implicits._
  
  def cgn[N: Field: Eq]: DirectedGraph[UnitOfMeasurement[Mass, N], N => N] = conversions(
    List(
      unit("milligram", "mg"),
      unit("gram", "g"),
      unit("kilogram", "Kg"),
      unit("megagram", "Mg")),
    (vs: Seq[Vertex[UnitOfMeasurement[Mass, N]]]) => vs match {
      case mg :: g :: kg :: megag :: Nil => List(
        (g, kg, (n: N) => n * 1000),
        (kg, g, (n: N) => n / 1000)
        )
      case _ => Nil
    })

  implicit val cgMassRational: DirectedGraph[UnitOfMeasurement[Mass, Rational], Rational => Rational] = cgn[Rational]
  implicit val cgMassReal: DirectedGraph[UnitOfMeasurement[Mass, Real], Real => Real] = cgn[Real]
  implicit val cgMassDouble: DirectedGraph[UnitOfMeasurement[Mass, Double], Double => Double] = cgn[Double]

  implicit val mtRational = modulize[Mass, Rational]
  implicit val mtReal = modulize[Mass, Real]
  implicit val mtDouble = modulize[Mass, Double]

  def milligram[N](implicit fieldN: Field[N], eqN: Eq[N], cg: DirectedGraph[UnitOfMeasurement[Mass, N], N => N]) = byName(cg, "milligram")
  def gram[N](implicit fieldN: Field[N], eqN: Eq[N], cg: DirectedGraph[UnitOfMeasurement[Mass, N], N => N]) = byName(cg, "gram")
  def kilogram[N](implicit fieldN: Field[N], eqN: Eq[N], cg: DirectedGraph[UnitOfMeasurement[Mass, N], N => N]) = byName(cg, "kilogram")
  def megagram[N](implicit fieldN: Field[N], eqN: Eq[N], cg: DirectedGraph[UnitOfMeasurement[Mass, N], N => N]) = byName(cg, "megagram")

}
