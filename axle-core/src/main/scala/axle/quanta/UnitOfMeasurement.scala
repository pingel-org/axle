package axle.quanta

import spire.algebra.Eq
import spire.algebra.MultiplicativeMonoid
import spire.implicits.StringOrder
import spire.implicits.eqOps

object UnitOfMeasurement4 {

  implicit def eqqqn[Q <: Quantum4[N], N: Eq]: Eq[UnitOfMeasurement4[Q, N]] =
    new Eq[UnitOfMeasurement4[Q, N]] {
      def eqv(x: UnitOfMeasurement4[Q, N], y: UnitOfMeasurement4[Q, N]): Boolean = x.name === y.name
    }
}

case class UnitOfMeasurement4[Q <: Quantum4[N], N](name: String, symbol: String, wikipediaUrl: Option[String]) {

  def magnitude(implicit ev: MultiplicativeMonoid[N]): N = ev.one

  def unit: UnitOfMeasurement4[Q, N] = this

  def *:(n: N) = UnittedQuantity4(n, unit)
}
