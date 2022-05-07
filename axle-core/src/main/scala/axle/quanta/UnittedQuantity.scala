package axle.quanta

import cats.Functor
import cats.Show
import cats.kernel.Eq
import cats.kernel.Order
import cats.implicits._
import cats.implicits.showInterpolator

import spire.algebra.MultiplicativeMonoid

object UnittedQuantity {

  implicit def showUQ[Q, N: Show]: Show[UnittedQuantity[Q, N]] = uq =>
    show"${uq.magnitude} ${uq.unit.symbol}"

  implicit def eqqqn[Q, N: Eq: MultiplicativeMonoid](implicit convert: UnitConverter[Q, N]): Eq[UnittedQuantity[Q, N]] =
    (x, y) =>
      ((x.unit == y.unit) && (x.magnitude === y.magnitude)) ||
        convert.convert(x, y.unit).magnitude === y.magnitude

  implicit def orderUQ[Q, N: MultiplicativeMonoid: Order](implicit convert: UnitConverter[Q, N]): Order[UnittedQuantity[Q, N]] =
    (x, y) =>
      Order[N].compare((x.in(y.unit)).magnitude, y.magnitude)

  implicit def functorUQ[Q]: Functor[Lambda[Z => UnittedQuantity[Q, Z]]] =
    new Functor[Lambda[Z => UnittedQuantity[Q, Z]]] {
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
