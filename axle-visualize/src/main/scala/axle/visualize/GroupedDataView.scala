package axle.visualize

import axle.algebra.Plottable
import axle.algebra.Zero
import scala.annotation.implicitNotFound
import spire.algebra.Order
import spire.compat.ordering

/**
 *
 * implicits for BarChartGrouped
 *
 */

@implicitNotFound("No member of typeclass GroupedDataView found for types ${G}, ${S}, ${Y}, ${D}")
trait GroupedDataView[G, S, Y, D] {

  def groups(d: D): Traversable[G]

  def slices(d: D): Traversable[S]

  def valueOf(d: D, gs: (G, S)): Y

  def yRange(d: D): (Y, Y)

}

object GroupedDataView {

  def apply[G, S, Y, D](implicit ev: GroupedDataView[G, S, Y, D]): GroupedDataView[G, S, Y, D] = ev
  
  implicit def mapGroupedDataView[G, S, Y: Plottable: Zero: Order]: GroupedDataView[G, S, Y, Map[(G, S), Y]] =
    new GroupedDataView[G, S, Y, Map[(G, S), Y]] {

      val yZero = Zero[Y]
      val yPlottable = Plottable[Y]

      def groups(d: Map[(G, S), Y]): Traversable[G] = d.keys.toList.map(_._1) // TODO: .sorted. cache

      def slices(d: Map[(G, S), Y]): Traversable[S] = d.keys.toList.map(_._2) // TODO: .sorted. cache

      def valueOf(d: Map[(G, S), Y], gs: (G, S)): Y = d.apply(gs)

      def yRange(d: Map[(G, S), Y]): (Y, Y) = {

        val minY = slices(d).map { s =>
          (groups(d).map { g => valueOf(d, (g, s)) } ++ List(yZero.zero))
            .filter(yPlottable.isPlottable _).min
        }.min

        val maxY = slices(d).map { s =>
          (groups(d).map { g => valueOf(d, (g, s)) } ++ List(yZero.zero))
            .filter(yPlottable.isPlottable _).max
        }.max

        (minY, maxY)
      }

    }

}