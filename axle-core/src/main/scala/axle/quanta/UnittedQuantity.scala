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

object UnittedQuantity {

  // Note: This Eq performs no conversion
  implicit def eqqqn[Q <: Quantum, N: Field: Eq]: Eq[UnittedQuantity[Q, N]] = new Eq[UnittedQuantity[Q, N]] {
    def eqv(x: UnittedQuantity[Q, N], y: UnittedQuantity[Q, N]): Boolean =
      (x.magnitude === y.magnitude) && (x.unit == y.unit)
  }

  implicit def orderUQ[Q <: Quantum, N: Order, DG[_, _]: DirectedGraph](implicit cg: DG[UnitOfMeasurement[Q, N], N => N]) = new Order[UnittedQuantity[Q, N]] {

    val orderN = implicitly[Order[N]]

    def compare(x: UnittedQuantity[Q, N], y: UnittedQuantity[Q, N]): Int =
      orderN.compare((x.in(y.unit)).magnitude, y.magnitude)
  }

}

case class UnittedQuantity[Q <: Quantum, N: Field: Eq](magnitude: N, unit: UnitOfMeasurement[Q, N])
{

  private[this] def vertex[DG[_, _]: DirectedGraph](cg: DG[UnitOfMeasurement[Q, N], N => N], query: UnitOfMeasurement[Q, N]): Vertex[UnitOfMeasurement[Q, N]] =
    cg.findVertex(_.payload.name === query.name).get

  def in[DG[_, _]: DirectedGraph](newUnit: UnitOfMeasurement[Q, N])(implicit cg: DG[UnitOfMeasurement[Q, N], N => N]): UnittedQuantity[Q, N] =
    cg.shortestPath(vertex(cg, newUnit), vertex(cg, unit))
      .map(
        _.map(_.payload).foldLeft(implicitly[Field[N]].one)((n, convert) => convert(n)))
      .map(n => UnittedQuantity((magnitude * n), newUnit))
      .getOrElse(throw new Exception("no conversion path from " + unit + " to " + newUnit))

  // TODO
  def over[QR <: Quantum, Q2 <: Quantum, N: Field: Eq](denominator: UnittedQuantity[QR, N]): UnitOfMeasurement[Q2, N] =
    UnitOfMeasurement[Q2, N]("TODO", "TODO", None)

}
