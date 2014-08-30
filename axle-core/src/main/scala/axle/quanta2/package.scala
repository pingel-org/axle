package axle

import axle.graph.Vertex
import axle.graph.DirectedGraph
import axle.graph.JungDirectedGraph
import scala.language.reflectiveCalls
import spire.math.Rational
import spire.algebra.{ Module, Field, Rng, Eq }
import spire.implicits.eqOps
import spire.implicits.moduleOps
import spire.implicits.groupOps
import spire.implicits.multiplicativeGroupOps
import spire.implicits.multiplicativeSemigroupOps
import spire.implicits.additiveGroupOps
import spire.implicits.additiveSemigroupOps

package object quanta2 {

  // Note: this is need for "def conversions"
  implicit def edgeEq[N: Eq]: Eq[N => N] = new Eq[N => N] {
    def eqv(x: N => N, y: N => N): Boolean = ???
  }

  implicit def modulize[Q <: Quantum, N](implicit fieldn: Field[N], eqn: Eq[N], cg: DirectedGraph[Quantity[Q, N], N => N]): Module[Quantity[Q, N], N] = new Module[Quantity[Q, N], N] {

    def negate(x: Quantity[Q, N]): Quantity[Q, N] = Quantity(-x.magnitude, x.unitOpt) // AdditiveGroup

    def zero: Quantity[Q, N] = Quantity(fieldn.zero, None) // AdditiveMonoid

    def plus(x: Quantity[Q, N], y: Quantity[Q, N]): Quantity[Q, N] =
      Quantity((x in y.unit).magnitude + y.magnitude, y.unitOpt) // AdditiveSemigroup

    implicit def scalar: Rng[N] = fieldn // Module

    def timesl(r: N, v: Quantity[Q, N]): Quantity[Q, N] = Quantity(v.magnitude * r, v.unitOpt)
  }

  def newUnit[Q <: Quantum, N: Field: Eq]: Quantity[Q, N] = Quantity(implicitly[Field[N]].one, None)

  def newUnitOfMeasurement[Q <: Quantum, N: Field: Eq](
    name: Option[String] = None,
    symbol: Option[String] = None,
    link: Option[String] = None): Quantity[Q, N] = Quantity[Q, N](implicitly[Field[N]].one, None, name, symbol, link)

  def unit[Q <: Quantum, N: Field: Eq](name: String, symbol: String, linkOpt: Option[String] = None): Quantity[Q, N] =
    newUnitOfMeasurement(Some(name), Some(symbol), linkOpt)

  private[quanta2] def conversions[Q <: Quantum, N: Field: Eq](vps: Seq[Quantity[Q, N]], ef: Seq[Vertex[Quantity[Q, N]]] => Seq[(Vertex[Quantity[Q, N]], Vertex[Quantity[Q, N]], N => N)]): DirectedGraph[Quantity[Q, N], N => N] =
    JungDirectedGraph(vps, ef)

  private[quanta2] def trip2fns[Q <: Quantum, N: Field: Eq](trip: (Vertex[Quantity[Q, N]], Vertex[Quantity[Q, N]], N)): Seq[(Vertex[Quantity[Q, N]], Vertex[Quantity[Q, N]], N => N)] = {
    val (from, to, multiplier) = trip
    Vector(
      (from, to, _ * multiplier),
      (to, from, _ / multiplier))
  }

  private[quanta2] def trips2fns[Q <: Quantum, N: Field: Eq](trips: Seq[(Vertex[Quantity[Q, N]], Vertex[Quantity[Q, N]], N)]) =
    trips.flatMap(trip2fns(_))

  private[quanta2] def byName[Q <: Quantum, N: Field: Eq](cg: DirectedGraph[Quantity[Q, N], N => N], unitName: String): Quantity[Q, N] =
    cg.findVertex(_.payload.name === unitName).get.payload

    
}