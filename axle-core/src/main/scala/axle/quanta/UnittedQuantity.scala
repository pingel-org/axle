package axle.quanta

import axle.algebra.Vertex
import spire.algebra.Eq
import spire.algebra.Field
import spire.algebra.Order
import spire.implicits.StringOrder
import spire.implicits.eqOps
import spire.implicits.multiplicativeSemigroupOps
import axle.algebra.DirectedGraph
import axle.syntax.directedgraph._

case class UnittedQuantity3[Q <: Quantum3, N: Field: Eq](magnitude: N, unit: UnitOfMeasurement3[Q, N]) {

  private[this] def vertex[DG[_, _]: DirectedGraph](
    cg: DG[UnitOfMeasurement3[Q, N], N => N], query: UnitOfMeasurement3[Q, N]): Vertex[UnitOfMeasurement3[Q, N]] =
    axle.syntax.directedgraph.directedGraphOps(cg).findVertex(_.payload.name === query.name).get

  def in[DG[_, _]: DirectedGraph](newUnit: UnitOfMeasurement3[Q, N])(implicit cg: DG[UnitOfMeasurement3[Q, N], N => N]): UnittedQuantity3[Q, N] =
    cg.shortestPath(vertex(cg, newUnit), vertex(cg, unit))
      .map(
        _.map(_.payload).foldLeft(implicitly[Field[N]].one)((n, convert) => convert(n)))
      .map(n => UnittedQuantity3((magnitude * n), newUnit))
      .getOrElse(throw new Exception("no conversion path from " + unit + " to " + newUnit))

  // TODO
  def over[QR <: Quantum3, Q2 <: Quantum3, N: Field: Eq](denominator: UnittedQuantity3[QR, N]): UnitOfMeasurement3[Q2, N] =
    UnitOfMeasurement3[Q2, N]("TODO", "TODO", None)

}

object UnittedQuantity3 {

  implicit def eqqqn[Q <: Quantum3, N: Field: Eq]: Eq[UnittedQuantity3[Q, N]] = new Eq[UnittedQuantity3[Q, N]] {
    def eqv(x: UnittedQuantity3[Q, N], y: UnittedQuantity3[Q, N]): Boolean =
      (x.magnitude === y.magnitude) && (x.unit == y.unit)
  }

  implicit def orderUQ[Q <: Quantum3, N: Order, DG[_, _]: DirectedGraph](implicit cg: DG[UnitOfMeasurement3[Q, N], N => N]) =
    new Order[UnittedQuantity3[Q, N]] {

      val orderN = implicitly[Order[N]]

      def compare(x: UnittedQuantity3[Q, N], y: UnittedQuantity3[Q, N]): Int =
        orderN.compare((x.in(y.unit)).magnitude, y.magnitude)
    }

}
