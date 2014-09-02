package axle

import axle.graph.Vertex
import axle.graph.DirectedGraph
import axle.graph.JungDirectedGraph
import scala.language.reflectiveCalls
import spire.math.Rational
import spire.math.Real
import spire.algebra.{ Module, Field, Rng, Eq, MetricSpace }
import spire.implicits.eqOps
import spire.implicits.moduleOps
import spire.implicits.groupOps
import spire.implicits.multiplicativeGroupOps
import spire.implicits.multiplicativeSemigroupOps
import spire.implicits.additiveGroupOps
import spire.implicits.additiveSemigroupOps

package object quanta2 {

  implicit val rationalDoubleMetricSpace: MetricSpace[Rational, Double] = new MetricSpace[Rational, Double] {

    def distance(v: Rational, w: Rational): Double = (v.toDouble - w.toDouble).abs

  }

  implicit val realDoubleMetricSpace: MetricSpace[Real, Double] = new MetricSpace[Real, Double] {

    def distance(v: Real, w: Real): Double = (v.toDouble - w.toDouble).abs

  }

  implicit val doubleDoubleMetricSpace: MetricSpace[Double, Double] = new MetricSpace[Double, Double] {

    def distance(v: Double, w: Double): Double = (v - w).abs

  }

  // Note: this is need for "def conversions"
  implicit def edgeEq[N: Eq]: Eq[N => N] = new Eq[N => N] {
    def eqv(x: N => N, y: N => N): Boolean = ???
  }

  implicit def modulize[Q <: Quantum, N](implicit fieldn: Field[N], eqn: Eq[N], cg: DirectedGraph[UnitOfMeasurement[Q, N], N => N]): Module[UnittedQuantity[Q, N], N] = new Module[UnittedQuantity[Q, N], N] {

    def negate(x: UnittedQuantity[Q, N]): UnittedQuantity[Q, N] = UnittedQuantity(-x.magnitude, x.unit) // AdditiveGroup

    def zero: UnittedQuantity[Q, N] = ??? // UnittedQuantity("zero", "zero", None) // AdditiveMonoid

    def plus(x: UnittedQuantity[Q, N], y: UnittedQuantity[Q, N]): UnittedQuantity[Q, N] =
      UnittedQuantity((x in y.unit).magnitude + y.magnitude, y.unit) // AdditiveSemigroup

    implicit def scalar: Rng[N] = fieldn // Module

    def timesl(r: N, v: UnittedQuantity[Q, N]): UnittedQuantity[Q, N] = UnittedQuantity(v.magnitude * r, v.unit)
  }

  def unit[Q <: Quantum, N: Field: Eq](name: String, symbol: String, linkOpt: Option[String] = None): UnitOfMeasurement[Q, N] =
    UnitOfMeasurement(name, symbol, linkOpt)

}