package axle.visualize

import spire.algebra.Order
import spire.compat.ordering
import axle.algebra.Plottable
import axle.algebra.Zero

/**
 * implicits for Plot and BarChart
 *
 */
trait DataView[X, Y, D] {

  def keys(d: D): Traversable[X]

  def valueOf(d: D, x: X): Y

  def yRange(d: D): (Y, Y)
}

object DataView {

  implicit def mapDataView[X, Y: Plottable: Zero: Order]: DataView[X, Y, Map[X, Y]] =
    new DataView[X, Y, Map[X, Y]] {

      val yPlottable = implicitly[Plottable[Y]]
      val yZero = implicitly[Zero[Y]]

      def keys(d: Map[X, Y]): Traversable[X] = d.keys

      def valueOf(d: Map[X, Y], x: X): Y = d(x)

      def yRange(d: Map[X, Y]): (Y, Y) = {

        val yMin = (keys(d).map { x => valueOf(d, x) } ++ List(yZero.zero)).filter(yPlottable.isPlottable _).min
        val yMax = (keys(d).map { x => valueOf(d, x) } ++ List(yZero.zero)).filter(yPlottable.isPlottable _).max

        (yMin, yMax)
      }

    }

}