package axle.visualize

import axle.algebra.Plottable
import axle.algebra.Zero
import scala.annotation.implicitNotFound
import spire.algebra.Order
import spire.compat.ordering

/**
 * implicits for Plot and BarChart
 *
 */

@implicitNotFound("No member of typeclass DataView found for types ${X}, ${Y}, ${D}")
trait DataView[X, Y, D] {

  def keys(d: D): Traversable[X]

  def valueOf(d: D, x: X): Y

  def yRange(d: D): (Y, Y)
}

object DataView {

  def apply[X, Y, D](implicit ev: DataView[X, Y, D]) = ev

  implicit def mapDataView[X, Y: Plottable: Zero: Order]: DataView[X, Y, Map[X, Y]] =
    new DataView[X, Y, Map[X, Y]] {

      val yPlottable = Plottable[Y]
      val yZero = Zero[Y]

      def keys(d: Map[X, Y]): Traversable[X] = d.keys

      def valueOf(d: Map[X, Y], x: X): Y = d(x)

      def yRange(d: Map[X, Y]): (Y, Y) = {

        val yMin = (keys(d).map { x => valueOf(d, x) } ++ List(yZero.zero)).filter(yPlottable.isPlottable _).min
        val yMax = (keys(d).map { x => valueOf(d, x) } ++ List(yZero.zero)).filter(yPlottable.isPlottable _).max

        (yMin, yMax)
      }

    }

}