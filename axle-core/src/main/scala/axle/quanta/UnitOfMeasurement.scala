package axle.quanta

import spire.algebra.Eq
import spire.algebra.MultiplicativeMonoid
import spire.implicits.StringOrder
import spire.implicits.eqOps

object UnitOfMeasurement {

  implicit def eqqqn[Q <: Quantum, N: Eq]: Eq[UnitOfMeasurement[Q, N]] = new Eq[UnitOfMeasurement[Q, N]] {
    def eqv(x: UnitOfMeasurement[Q, N], y: UnitOfMeasurement[Q, N]): Boolean = x.name === y.name
  }
}

case class UnitOfMeasurement[Q <: Quantum, N](name: String, symbol: String, wikipediaUrl: Option[String]) {

  def magnitude(implicit ev: MultiplicativeMonoid[N]): N = ev.one

  def unit: UnitOfMeasurement[Q, N] = this

  def *:(n: N) = UnittedQuantity(n, unit)
}
