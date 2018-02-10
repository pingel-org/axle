package axle.quanta

import cats.Functor
import cats.Show
import cats.kernel.Eq
import cats.kernel.Order
import cats.implicits._

import spire.algebra.MultiplicativeMonoid

import axle.string

object UnittedQuantity {

  implicit def showUQ[Q, N: Show]: Show[UnittedQuantity[Q, N]] =
    new Show[UnittedQuantity[Q, N]] {
      def show(uq: UnittedQuantity[Q, N]): String = string(uq.magnitude) + " " + uq.unit.symbol
    }

  implicit def eqqqn[Q, N: Eq: MultiplicativeMonoid](implicit convert: UnitConverter[Q, N]): Eq[UnittedQuantity[Q, N]] =
    new Eq[UnittedQuantity[Q, N]] {
      def eqv(x: UnittedQuantity[Q, N], y: UnittedQuantity[Q, N]): Boolean =
        ((x.unit == y.unit) && (x.magnitude === y.magnitude)) ||
          convert.convert(x, y.unit).magnitude === y.magnitude
    }

  implicit def orderUQ[Q, N: MultiplicativeMonoid: Order](implicit convert: UnitConverter[Q, N]) =
    new Order[UnittedQuantity[Q, N]] {

      def compare(x: UnittedQuantity[Q, N], y: UnittedQuantity[Q, N]): Int =
        Order[N].compare((x.in(y.unit)).magnitude, y.magnitude)
    }

  implicit def functorUQ[Q]: Functor[({ type λ[α] = UnittedQuantity[Q, α] })#λ] =
    new Functor[({ type λ[α] = UnittedQuantity[Q, α] })#λ] {
      def map[A, B](uq: UnittedQuantity[Q, A])(f: A => B): UnittedQuantity[Q, B] =
        UnittedQuantity(f(uq.magnitude), uq.unit)
    }

}

case class UnittedQuantity[Q, N](magnitude: N, unit: UnitOfMeasurement[Q]) {

  def in(newUnit: UnitOfMeasurement[Q])(
    implicit
    convert: UnitConverter[Q, N], ev: MultiplicativeMonoid[N]): UnittedQuantity[Q, N] =
    convert.convert(this, newUnit)

  // TODO
  def over[QR, Q2, M](denominator: UnittedQuantity[QR, M]): UnitOfMeasurement[Q2] =
    UnitOfMeasurement[Q2]("TODO", "TODO", None)

}
