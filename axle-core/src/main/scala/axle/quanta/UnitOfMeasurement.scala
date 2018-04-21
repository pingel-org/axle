package axle.quanta

import cats.Show
import cats.kernel.Eq

object UnitOfMeasurement {

  implicit def eqqqn[Q] = Eq.fromUniversalEquals[UnitOfMeasurement[Q]]

  implicit def showUoM[T]: Show[UnitOfMeasurement[T]] =
    u => s"${u.name} (${u.symbol})"

}

case class UnitOfMeasurement[Q](name: String, symbol: String, wikipediaUrl: Option[String]) {

  def *:[N](n: N) = UnittedQuantity(n, this)
}
