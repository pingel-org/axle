package axle.quanta

import axle.algebra.DirectedGraph
import axle.algebra.Vertex
import axle.syntax.directedgraph.directedGraphOps
import spire.algebra.Eq
import spire.algebra.MultiplicativeMonoid
import spire.algebra.Order
import spire.implicits.StringOrder
import spire.implicits.eqOps
import spire.implicits.multiplicativeSemigroupOps

object UnittedQuantity {

  implicit def eqqqn[Q <: Quantum, N: Eq]: Eq[UnittedQuantity[Q, N]] =
    new Eq[UnittedQuantity[Q, N]] {
      def eqv(x: UnittedQuantity[Q, N], y: UnittedQuantity[Q, N]): Boolean =
        (x.magnitude === y.magnitude) && (x.unit == y.unit)
    }

  implicit def orderUQ[Q <: Quantum, N: MultiplicativeMonoid: Order, DG[_, _]: DirectedGraph](
    implicit meta: QuantumMetadata[Q, N, DG]) =
    new Order[UnittedQuantity[Q, N]] {

      val orderN = implicitly[Order[N]]

      def compare(x: UnittedQuantity[Q, N], y: UnittedQuantity[Q, N]): Int =
        orderN.compare((x.in(y.unit)).magnitude, y.magnitude)
    }

}

case class UnittedQuantity[Q, N](magnitude: N, unit: UnitOfMeasurement[Q, N]) {

  // TODO: create a Functor witness for UnittedQuantity
  def map[B, DG[_, _]: DirectedGraph](f: N => B)(implicit meta: QuantumMetadata[Q, B, DG]): UnittedQuantity[Q, B] =
    UnittedQuantity(f(magnitude), ???)

  private[this] def vertex[DG[_, _]: DirectedGraph](
    cg: DG[UnitOfMeasurement[Q, N], N => N],
    query: UnitOfMeasurement[Q, N])(
      implicit ev: Eq[N]): Vertex[UnitOfMeasurement[Q, N]] =
    axle.syntax.directedgraph.directedGraphOps(cg).findVertex(_.payload.name === query.name).get

  def in[DG[_, _]: DirectedGraph](
    newUnit: UnitOfMeasurement[Q, N])(
      implicit meta: QuantumMetadata[Q, N, DG], ev: MultiplicativeMonoid[N], ev2: Eq[N]): UnittedQuantity[Q, N] =
    directedGraphOps(meta.conversionGraph).shortestPath(vertex(meta.conversionGraph, newUnit), vertex(meta.conversionGraph, unit))
      .map(
        _.map(_.payload).foldLeft(ev.one)((n, convert) => convert(n)))
      .map(n => UnittedQuantity((magnitude * n), newUnit))
      .getOrElse(throw new Exception("no conversion path from " + unit + " to " + newUnit))

  // TODO
  def over[QR, Q2, N](denominator: UnittedQuantity[QR, N]): UnitOfMeasurement[Q2, N] =
    UnitOfMeasurement[Q2, N]("TODO", "TODO", None)

}
