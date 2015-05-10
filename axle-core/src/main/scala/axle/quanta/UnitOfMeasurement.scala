package axle.quanta

import axle.Show
import spire.algebra.Eq
import spire.implicits.StringOrder
import spire.implicits.eqOps

object UnitOfMeasurement {

  implicit def eqqqn[Q]: Eq[UnitOfMeasurement[Q]] =
    new Eq[UnitOfMeasurement[Q]] {
      def eqv(x: UnitOfMeasurement[Q], y: UnitOfMeasurement[Q]): Boolean = x.equals(y)
    }

  implicit def showUoM[T]: Show[UnitOfMeasurement[T]] =
    new Show[UnitOfMeasurement[T]] {
      def text(u: UnitOfMeasurement[T]): String = s"${u.name} (${u.symbol}) "
    }

}

case class UnitOfMeasurement[Q](name: String, symbol: String, wikipediaUrl: Option[String]) {

  def *:[N](n: N) = UnittedQuantity(n, this)
}
