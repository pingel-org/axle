package axle.quanta

import axle.algebra.Plottable
import axle.graph.DirectedGraph
import spire.algebra.Field
import spire.algebra.MetricSpace
import spire.algebra.Order
import spire.algebra.Module
import spire.implicits.moduleOps

case class UnitPlottable[Q <: Quantum, N: Field: Order](base: UnitOfMeasurement[Q, N])(implicit space: MetricSpace[N, Double], cg: DirectedGraph[UnitOfMeasurement[Q, N], N => N], module: Module[UnittedQuantity[Q, N], N])
  extends Plottable[UnittedQuantity[Q, N]] {

  val underlying = Plottable.abstractAlgebraPlottable[N]
  val field = implicitly[Field[N]]

  def isPlottable(t: UnittedQuantity[Q, N]): Boolean = underlying.isPlottable(t.magnitude)

  def zero: UnittedQuantity[Q, N] = field.zero *: base

  def compare(u1: UnittedQuantity[Q, N], u2: UnittedQuantity[Q, N]): Int =
    underlying.compare((u1 in base).magnitude, (u2 in base).magnitude)

  def portion(left: UnittedQuantity[Q, N], v: UnittedQuantity[Q, N], right: UnittedQuantity[Q, N]): Double =
    underlying.portion((left in base).magnitude, (v in base).magnitude, (right in base).magnitude)

  def tics(from: UnittedQuantity[Q, N], to: UnittedQuantity[Q, N]): Seq[(UnittedQuantity[Q, N], String)] =
    underlying.tics((from in base).magnitude, (to in base).magnitude) map {
      case (v, label) =>
        (v *: base, v.toString)
    }

}
