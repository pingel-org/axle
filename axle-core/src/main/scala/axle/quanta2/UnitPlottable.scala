package axle.quanta2

import axle.algebra.Plottable
import axle.graph.DirectedGraph
import spire.algebra.Field
import spire.algebra.MetricSpace
import spire.algebra.Order
import spire.implicits.moduleOps

case class UnitPlottable[Q <: Quantum, N: Field: Order](base: Quantity[Q, N])(implicit space: MetricSpace[N, Double], cg: DirectedGraph[Quantity[Q, N], N => N])
  extends Plottable[Quantity[Q, N]] {

  val underlying = Plottable.abstractAlgebraPlottable[N]
  val field = implicitly[Field[N]]

  def isPlottable(t: Quantity[Q, N]): Boolean = underlying.isPlottable(t.magnitude)

  def zero: Quantity[Q, N] = field.zero *: base

  def compare(u1: Quantity[Q, N], u2: Quantity[Q, N]): Int =
    underlying.compare((u1 in base).magnitude, (u2 in base).magnitude)

  def portion(left: Quantity[Q, N], v: Quantity[Q, N], right: Quantity[Q, N]): Double =
    underlying.portion((left in base).magnitude, (v in base).magnitude, (right in base).magnitude)

  def tics(from: Quantity[Q, N], to: Quantity[Q, N]): Seq[(Quantity[Q, N], String)] =
    underlying.tics((from in base).magnitude, (to in base).magnitude) map {
      case (v, label) =>
        (v *: base, v.toString)
    }

}
