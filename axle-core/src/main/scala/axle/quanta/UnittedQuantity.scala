package axle.quanta

import axle.algebra.Vertex
import axle.Show
import axle.algebra.Functor
import axle.syntax.directedgraph.directedGraphOps
import scala.reflect.ClassTag
import spire.algebra.Eq
import spire.algebra.MultiplicativeMonoid
import spire.algebra.Order
import spire.implicits.StringOrder
import spire.implicits.eqOps
import spire.implicits.multiplicativeSemigroupOps

object UnittedQuantity {

  implicit def showEQ[Q, N]: Show[UnittedQuantity[Q, N]] =
    new Show[UnittedQuantity[Q, N]] {
      def text(uq: UnittedQuantity[Q, N]): String = uq.magnitude + " " + uq.unit.symbol
    }

  implicit def eqqqn[Q, N: Eq]: Eq[UnittedQuantity[Q, N]] =
    new Eq[UnittedQuantity[Q, N]] {
      def eqv(x: UnittedQuantity[Q, N], y: UnittedQuantity[Q, N]): Boolean =
        (x.magnitude === y.magnitude) && (x.unit == y.unit)
    }

  implicit def orderUQ[Q, N: MultiplicativeMonoid: Order](implicit convert: UnitConverter[Q, N]) =
    new Order[UnittedQuantity[Q, N]] {

      val orderN = implicitly[Order[N]]

      def compare(x: UnittedQuantity[Q, N], y: UnittedQuantity[Q, N]): Int =
        orderN.compare((x.in(y.unit)).magnitude, y.magnitude)
    }

  implicit def functorUQ[Q]: Functor[({ type λ[α] = UnittedQuantity[Q, α] })#λ] =
    new Functor[({ type λ[α] = UnittedQuantity[Q, α] })#λ] {
      def map[N, B: ClassTag](uq: UnittedQuantity[Q, N])(f: N => B) =
        UnittedQuantity(f(uq.magnitude), uq.unit)
    }

}

case class UnittedQuantity[Q, N](magnitude: N, unit: UnitOfMeasurement[Q]) {

  def in(newUnit: UnitOfMeasurement[Q])(
    implicit convert: UnitConverter[Q, N], ev: MultiplicativeMonoid[N], ev2: Eq[N]): UnittedQuantity[Q, N] =
    convert.convert(this, newUnit)

  // TODO
  def over[QR, Q2, N](denominator: UnittedQuantity[QR, N]): UnitOfMeasurement[Q2] =
    UnitOfMeasurement[Q2]("TODO", "TODO", None)

}
