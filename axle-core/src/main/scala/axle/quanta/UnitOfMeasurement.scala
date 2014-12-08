package axle.quanta

import spire.algebra.Eq
import spire.algebra.Field
import spire.implicits._

object UnitOfMeasurement {

  implicit def eqqqn[Q <: Quantum, N: Field: Eq]: Eq[UnitOfMeasurement[Q, N]] = new Eq[UnitOfMeasurement[Q, N]] {
    def eqv(x: UnitOfMeasurement[Q, N], y: UnitOfMeasurement[Q, N]): Boolean = x.name === y.name
  }

}

case class UnitOfMeasurement[Q <: Quantum, N: Field: Eq](name: String, symbol: String, wikipediaUrl: Option[String]) {

  def magnitude: N = implicitly[Field[N]].one

  def unit: UnitOfMeasurement[Q, N] = this

  def *:(n: N) = UnittedQuantity(n, this)
}

object UnitOfMeasurement3 {
  implicit def eqqqn[Q <: Quantum3, N: Field: Eq]: Eq[UnitOfMeasurement3[Q, N]] = new Eq[UnitOfMeasurement3[Q, N]] {
    def eqv(x: UnitOfMeasurement3[Q, N], y: UnitOfMeasurement3[Q, N]): Boolean = x.name === y.name
  }
}

case class UnitOfMeasurement3[Q <: Quantum3, N: Field: Eq](name: String, symbol: String, wikipediaUrl: Option[String]) {

  def magnitude: N = implicitly[Field[N]].one

  def unit: UnitOfMeasurement3[Q, N] = this

  def *:(n: N) = UnittedQuantity3(n, unit)
}
