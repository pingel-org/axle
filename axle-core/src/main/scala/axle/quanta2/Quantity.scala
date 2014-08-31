package axle.quanta2

import axle.graph.DirectedGraph
import axle.graph.Vertex
import spire.algebra.Field
import spire.algebra.Eq
import spire.algebra.Order
import spire.implicits.eqOps
import spire.implicits.multiplicativeSemigroupOps
import spire.implicits.multiplicativeGroupOps

object Quantity {

  implicit def orderQuantity[Q <: Quantum, N: Order](implicit cg: DirectedGraph[Quantity[Q, N], N => N]) = new Order[Quantity[Q, N]] {
    val orderN = implicitly[Order[N]]
    def compare(x: Quantity[Q, N], y: Quantity[Q, N]): Int =
      orderN.compare((x in y.unit).magnitude, y.magnitude)
  }

  // Note: This Eq performs no conversion
  implicit def eqqqn[Q <: Quantum, N: Field: Eq]: Eq[Quantity[Q, N]] = new Eq[Quantity[Q, N]] {
    def eqv(x: Quantity[Q, N], y: Quantity[Q, N]): Boolean =
      (x.magnitude === y.magnitude) && (x.unit == y.unit) 
  }
}

case class Quantity[Q <: Quantum, N](
  magnitude: N, unitOpt: Option[Quantity[Q, N]] = None, nameOpt: Option[String] = None, symbol: Option[String] = None, _link: Option[String] = None)(implicit fieldN: Field[N], eqN: Eq[N]) {

  def unit: Quantity[Q, N] = unitOpt.getOrElse(this)

  def name: String = nameOpt.getOrElse("")

  def label: String = nameOpt.getOrElse("")

  private[this] def vertex(cg: DirectedGraph[Quantity[Q, N], N => N], quantity: Quantity[Q, N]): Vertex[Quantity[Q, N]] = {
    cg.findVertex(_.payload === quantity).get
  }

  def in(newUnit: Quantity[Q, N])(implicit cg: DirectedGraph[Quantity[Q, N], N => N]): Quantity[Q, N] =
    cg.shortestPath(vertex(cg, newUnit.unit), vertex(cg, unit))
      .map(
        _.map(_.payload).foldLeft(implicitly[Field[N]].one)((n, convert) => convert(n)))
      .map(n => Quantity((magnitude * n) / newUnit.magnitude, Some(newUnit)))
      .getOrElse(throw new Exception("no conversion path from " + this + " to " + newUnit))

  // TODO
  def over[QR <: Quantum, Q2 <: Quantum, N: Field: Eq](denominator: Quantity[QR, N]): Quantity[Q2, N] =
    newUnit[Q2, N]

}
