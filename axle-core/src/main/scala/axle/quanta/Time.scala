package axle.quanta

import axle.graph.DirectedGraph
import axle.graph.Vertex
import spire.algebra.Eq
import spire.algebra.Field
import spire.implicits.DoubleAlgebra
import spire.implicits.eqOps
import spire.implicits.moduleOps
import spire.implicits.groupOps
import spire.implicits.multiplicativeGroupOps
import spire.implicits.multiplicativeSemigroupOps
import spire.implicits.additiveGroupOps
import spire.implicits.additiveSemigroupOps
import spire.implicits._
import spire.math.Rational
import spire.math.Real

abstract class Time extends Quantum {
  def wikipediaUrl = "http://en.wikipedia.org/wiki/Orders_of_magnitude_(time)"
}

object Time extends Time {

  import spire.implicits._

  type Q = Time

  def units[N: Field: Eq] = List[UnitOfMeasurement[Q, N]](
    unit("millisecond", "ms"),
    unit("second", "s"),
    unit("minute", "min"))

  def links[N: Field: Eq] = {
    implicit val baseCG = cgnDisconnected[N]
    List[(UnitOfMeasurement[Q, N], UnitOfMeasurement[Q, N], N => N, N => N)](
      (millisecond, second, _ * 1000, _ / 1000),
      (second, minute, _ * 60, _ / 60))
  }

  def millisecond[N: Field: Eq](implicit cg: CG[N]) = byName(cg, "millisecond")
  def second[N: Field: Eq](implicit cg: CG[N]) = byName(cg, "second")
  def minute[N: Field: Eq](implicit cg: CG[N]) = byName(cg, "minute")

}
