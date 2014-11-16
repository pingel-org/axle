package axle.quanta

import axle.algebra.LengthSpace
import axle.algebra.Plottable
import axle.algebra.Tics
import axle.algebra.Zero
import axle.graph.DirectedGraph
import axle.Show
import axle.string
import spire.algebra.Field
import spire.algebra.Module
import spire.algebra.Order
import spire.implicits.additiveSemigroupOps
import spire.implicits.multiplicativeSemigroupOps
import spire.implicits.signedOps

class UnitOrder[Q <: Quantum, N: Field: Order](base: UnitOfMeasurement[Q, N])(implicit cg: DirectedGraph[UnitOfMeasurement[Q, N], N => N])
  extends Order[UnittedQuantity[Q, N]] {

  val underlying = implicitly[Order[N]]

  def compare(u1: UnittedQuantity[Q, N], u2: UnittedQuantity[Q, N]): Int =
    underlying.compare((u1 in base).magnitude, (u2 in base).magnitude)
}

class UnittedPlottable[Q <: Quantum, N](implicit up: Plottable[N]) extends Plottable[UnittedQuantity[Q, N]] {

  override def isPlottable(t: UnittedQuantity[Q, N]): Boolean = up.isPlottable(t.magnitude)
}

class UnittedZero[Q <: Quantum, N: Field](base: UnitOfMeasurement[Q, N])(implicit cg: DirectedGraph[UnitOfMeasurement[Q, N], N => N])
  extends Zero[UnittedQuantity[Q, N]] {

  val field = implicitly[Field[N]]

  def zero: UnittedQuantity[Q, N] = field.zero *: base

}

class UnittedTics[Q <: Quantum, N: Tics: Show](
  base: UnitOfMeasurement[Q, N])(
    implicit cg: DirectedGraph[UnitOfMeasurement[Q, N], N => N])
  extends Tics[UnittedQuantity[Q, N]] {

  val underlying = implicitly[Tics[N]]

  def tics(from: UnittedQuantity[Q, N], to: UnittedQuantity[Q, N]): Seq[(UnittedQuantity[Q, N], String)] =
    underlying.tics((from in base).magnitude, (to in base).magnitude) map {
      case (v, label) =>
        (v *: base, string(v))
    }
}

class UnittedLengthSpace[Q <: Quantum, N: Field: Order](
  base: UnitOfMeasurement[Q, N])(
    implicit space: LengthSpace[N, Double],
    cg: DirectedGraph[UnitOfMeasurement[Q, N], N => N],
    module: Module[UnittedQuantity[Q, N], N])
  extends LengthSpace[UnittedQuantity[Q, N], UnittedQuantity[Q, N]] {

  val field = implicitly[Field[N]]

  def distance(v: UnittedQuantity[Q, N], w: UnittedQuantity[Q, N]): UnittedQuantity[Q, N] =
    (field.minus((v in base).magnitude, (w in base).magnitude).abs) *: base

  def onPath(left: UnittedQuantity[Q, N], right: UnittedQuantity[Q, N], p: Double): UnittedQuantity[Q, N] =
    ((field.minus((right in base).magnitude, (left in base).magnitude)) * p + (left in base).magnitude) *: base

  def portion(left: UnittedQuantity[Q, N], v: UnittedQuantity[Q, N], right: UnittedQuantity[Q, N]): Double =
    space.portion((left in base).magnitude, (v in base).magnitude, (right in base).magnitude)

}
