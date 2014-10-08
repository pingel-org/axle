package axle.visualize

import spire.algebra.Order
import spire.compat.ordering
import axle.algebra.Plottable

/**
 * implicits for Plot and BarChart
 *
 */
trait DataView[X, Y, D] {

  def keys(d: D): Traversable[X]

  def valueOf(d: D, x: X): Y

  def yRange(d: D, plottable: Plottable[Y]): (Y, Y)
}

object DataView {

  implicit def mapDataView[X, Y]: DataView[X, Y, Map[X, Y]] =
    new DataView[X, Y, Map[X, Y]] {

      def keys(d: Map[X, Y]): Traversable[X] = d.keys

      def valueOf(d: Map[X, Y], x: X): Y = d.apply(x)

      def yRange(d: Map[X, Y], plottable: Plottable[Y]): (Y, Y) = {
        implicit val order = plottable.order

        val yMin = (keys(d).map { x => valueOf(d, x) } ++ List(plottable.zero)).filter(plottable.isPlottable).min
        val yMax = (keys(d).map { x => valueOf(d, x) } ++ List(plottable.zero)).filter(plottable.isPlottable).max

        (yMin, yMax)
      }

    }

}