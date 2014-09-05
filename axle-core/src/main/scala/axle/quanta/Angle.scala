package axle.quanta

import axle.graph.DirectedGraph
import axle.graph.Vertex
import spire.algebra.Eq
import spire.algebra.Field
import spire.implicits.DoubleAlgebra
import spire.implicits.FloatAlgebra
import spire.math.Rational
import spire.math.Real

abstract class Angle extends Quantum {
  def wikipediaUrl = "http://en.wikipedia.org/wiki/Degree_(angle)"
}

object Angle extends Angle {

  type Q = Angle

  def units[N: Field: Eq] = List[UnitOfMeasurement[Q, N]](
    unit("degree", "°"),
    unit("radian", "rad"))

  def links[N: Field: Eq] = List.empty[(UnitOfMeasurement[Q, N], UnitOfMeasurement[Q, N], N => N, N => N)]

  def degree[N: Field: Eq](implicit cg: CG[N]) = byName(cg, "degree")
  def °[N: Field: Eq](implicit cg: CG[N]) = byName(cg, "degree")
  def radian[N: Field: Eq](implicit cg: CG[N]) = byName(cg, "radian")
  def rad[N: Field: Eq](implicit cg: CG[N]) = byName(cg, "radian")

}
