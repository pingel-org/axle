package axle.quanta2

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

class Time extends Quantum {
  def wikipediaUrl = "http://en.wikipedia.org/wiki/Orders_of_magnitude_(time)"
}

object Time extends Time {

  import spire.implicits._

  def cgn[N: Field: Eq]: DirectedGraph[UnitOfMeasurement[Time, N], N => N] = conversions(
    List(
      unit("millisecond", "ms"),
      unit("second", "s"),
      unit("minute", "min")),
    (vs: Seq[Vertex[UnitOfMeasurement[Time, N]]]) => vs match {
      case ms :: s :: min :: Nil =>
        (ms, s, (ms: N) => ms / 1000) ::
          (s, ms, (s: N) => s * 1000) ::
          (s, min, (s: N) => s / 60) ::
          (min, s, (min: N) => min * 60) ::
          Nil
      case _ => Nil
    })

  implicit val cgTimeRational: DirectedGraph[UnitOfMeasurement[Time, Rational], Rational => Rational] = cgn[Rational]
  implicit val cgTimeReal: DirectedGraph[UnitOfMeasurement[Time, Real], Real => Real] = cgn[Real]
  implicit val cgTimeDouble: DirectedGraph[UnitOfMeasurement[Time, Double], Double => Double] = cgn[Double]

  implicit val mtRational = modulize[Time, Rational]
  implicit val mtReal = modulize[Time, Real]
  implicit val mtDouble = modulize[Time, Double]

  def millisecond[N](implicit fieldN: Field[N], eqN: Eq[N], cg: DirectedGraph[UnitOfMeasurement[Time, N], N => N]) = byName(cg, "millisecond")
  def second[N](implicit fieldN: Field[N], eqN: Eq[N], cg: DirectedGraph[UnitOfMeasurement[Time, N], N => N]) = byName(cg, "second")
  def minute[N](implicit fieldN: Field[N], eqN: Eq[N], cg: DirectedGraph[UnitOfMeasurement[Time, N], N => N]) = byName(cg, "minute")

}
