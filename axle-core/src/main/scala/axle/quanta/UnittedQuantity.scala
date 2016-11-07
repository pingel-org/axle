package axle.quanta

import cats.Show
import axle.string
import axle.algebra.Functor
import spire.algebra.Eq
import spire.algebra.MultiplicativeMonoid
import spire.algebra.Order
import spire.implicits.eqOps

object UnittedQuantity {

  implicit def showUQ[Q, N: Show]: Show[UnittedQuantity[Q, N]] =
    new Show[UnittedQuantity[Q, N]] {
      def show(uq: UnittedQuantity[Q, N]): String = string(uq.magnitude) + " " + uq.unit.symbol
    }

  implicit def eqqqn[Q, N: Eq]: Eq[UnittedQuantity[Q, N]] =
    new Eq[UnittedQuantity[Q, N]] {
      def eqv(x: UnittedQuantity[Q, N], y: UnittedQuantity[Q, N]): Boolean =
        (x.magnitude === y.magnitude) && (x.unit == y.unit)
    }

  implicit def orderUQ[Q, N: MultiplicativeMonoid: Order](implicit convert: UnitConverter[Q, N]) =
    new Order[UnittedQuantity[Q, N]] {

      def compare(x: UnittedQuantity[Q, N], y: UnittedQuantity[Q, N]): Int =
        Order[N].compare((x.in(y.unit)).magnitude, y.magnitude)
    }

  // ({ type λ[α] = UnittedQuantity[Q, α] })#λ
  implicit def functorUQ[Q, A, B]: Functor[UnittedQuantity[Q, A], A, B, UnittedQuantity[Q, B]] =
    new Functor[UnittedQuantity[Q, A], A, B, UnittedQuantity[Q, B]] {
      def map(uq: UnittedQuantity[Q, A])(f: A => B): UnittedQuantity[Q, B] =
        UnittedQuantity(f(uq.magnitude), uq.unit)
    }

}

case class UnittedQuantity[Q, N](magnitude: N, unit: UnitOfMeasurement[Q]) {

  def in(newUnit: UnitOfMeasurement[Q])(
    implicit convert: UnitConverter[Q, N], ev: MultiplicativeMonoid[N]): UnittedQuantity[Q, N] =
    convert.convert(this, newUnit)

  // TODO
  def over[QR, Q2, M](denominator: UnittedQuantity[QR, M]): UnitOfMeasurement[Q2] =
    UnitOfMeasurement[Q2]("TODO", "TODO", None)

}
