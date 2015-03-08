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

  implicit def eqqqn[Q <: Quantum[N], N: Eq]: Eq[UnittedQuantity[Q, N]] =
    new Eq[UnittedQuantity[Q, N]] {
      def eqv(x: UnittedQuantity[Q, N], y: UnittedQuantity[Q, N]): Boolean =
        (x.magnitude === y.magnitude) && (x.unit == y.unit)
    }

  implicit def orderUQ[Q <: Quantum[N], N: MultiplicativeMonoid: Order, DG[_, _]: DirectedGraph](implicit cg: DG[UnitOfMeasurement[Q, N], N => N]) =
    new Order[UnittedQuantity[Q, N]] {

      val orderN = implicitly[Order[N]]

      def compare(x: UnittedQuantity[Q, N], y: UnittedQuantity[Q, N]): Int =
        orderN.compare((x.in(y.unit)).magnitude, y.magnitude)
    }

}

case class UnittedQuantity[Q <: Quantum[N], N](magnitude: N, unit: UnitOfMeasurement[Q, N]) {

  def map[B](f: N => B) = ??? //: UnittedQuantity[Q, B] = ??? //UnittedQuantity(f(magnitude), unit)

  private[this] def vertex[DG[_, _]: DirectedGraph](
    cg: DG[UnitOfMeasurement[Q, N], N => N],
    query: UnitOfMeasurement[Q, N])(
      implicit ev: Eq[N]): Vertex[UnitOfMeasurement[Q, N]] =
    axle.syntax.directedgraph.directedGraphOps(cg).findVertex(_.payload.name === query.name).get

  def in[DG[_, _]: DirectedGraph](
    newUnit: UnitOfMeasurement[Q, N])(
      implicit cg: DG[UnitOfMeasurement[Q, N], N => N], ev: MultiplicativeMonoid[N], ev2: Eq[N]): UnittedQuantity[Q, N] =
    directedGraphOps(cg).shortestPath(vertex(cg, newUnit), vertex(cg, unit))
      .map(
        _.map(_.payload).foldLeft(ev.one)((n, convert) => convert(n)))
      .map(n => UnittedQuantity((magnitude * n), newUnit))
      .getOrElse(throw new Exception("no conversion path from " + unit + " to " + newUnit))

  // TODO
  def over[QR <: Quantum[N], Q2 <: Quantum[N], N](denominator: UnittedQuantity[QR, N]): UnitOfMeasurement[Q2, N] =
    UnitOfMeasurement[Q2, N]("TODO", "TODO", None)

}
