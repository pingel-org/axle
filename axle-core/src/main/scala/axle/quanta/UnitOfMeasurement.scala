package axle.quanta

import cats.Show
import cats.kernel.Eq

object UnitOfMeasurement {

  implicit def eqqqn[Q]: Eq[UnitOfMeasurement[Q]] =
    (x, y) => x.equals(y)

  implicit def showUoM[T]: Show[UnitOfMeasurement[T]] =
    u => s"${u.name} (${u.symbol})"

}

case class UnitOfMeasurement[Q](name: String, symbol: String, wikipediaUrl: Option[String]) {

  def *:[N](n: N) = UnittedQuantity(n, this)
}
