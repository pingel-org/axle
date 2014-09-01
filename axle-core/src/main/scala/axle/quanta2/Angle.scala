package axle.quanta2

import axle.graph.DirectedGraph
import axle.graph.Vertex
import spire.algebra.Eq
import spire.algebra.Field
import spire.implicits.DoubleAlgebra
import spire.implicits.FloatAlgebra 
import spire.math.Rational
import spire.math.Real

class Angle extends Quantum {
  def wikipediaUrl = "http://en.wikipedia.org/wiki/Degree_(angle)"
}

object Angle extends Angle {

  def cgn[N: Field: Eq]: DirectedGraph[UnitOfMeasurement[Angle, N], N => N] = conversions(
    List(
      unit("degree", "°"),
      unit("radian", "rad")),
    (vs: Seq[Vertex[UnitOfMeasurement[Angle, N]]]) => vs match {
      case s :: Nil => List()
      case _ => Nil
    })

  implicit val cgAngleRational: DirectedGraph[UnitOfMeasurement[Angle, Rational], Rational => Rational] = cgn[Rational]
  implicit val cgAngleReal: DirectedGraph[UnitOfMeasurement[Angle, Real], Real => Real] = cgn[Real]
  implicit val cgAngleDouble: DirectedGraph[UnitOfMeasurement[Angle, Double], Double => Double] = cgn[Double]
  implicit val cgAngleFloat: DirectedGraph[UnitOfMeasurement[Angle, Float], Float => Float] = cgn[Float]

  implicit val mtRational = modulize[Angle, Rational]
  implicit val mtReal = modulize[Angle, Real]
  implicit val mtDouble = modulize[Angle, Double]
  implicit val mtFloat = modulize[Angle, Float]

  def degree[N](implicit fieldN: Field[N], eqN: Eq[N], cg: DirectedGraph[UnitOfMeasurement[Angle, N], N => N]) = byName(cg, "degree")
  def °[N](implicit fieldN: Field[N], eqN: Eq[N], cg: DirectedGraph[UnitOfMeasurement[Angle, N], N => N]) = byName(cg, "degree")
  def radian[N](implicit fieldN: Field[N], eqN: Eq[N], cg: DirectedGraph[UnitOfMeasurement[Angle, N], N => N]) = byName(cg, "radian")
  def rad[N](implicit fieldN: Field[N], eqN: Eq[N], cg: DirectedGraph[UnitOfMeasurement[Angle, N], N => N]) = byName(cg, "radian")
  
}
