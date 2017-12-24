package axle.visualize

import scala.annotation.implicitNotFound
import spire.algebra.AdditiveMonoid
import cats.kernel.Order
import cats.implicits._
import axle.algebra.Plottable

/**
 *
 * implicits for BarChartGrouped
 *
 */

@implicitNotFound("Witness not found for GroupedDataView[${G}, ${S}, ${Y}, ${D}]")
trait GroupedDataView[G, S, Y, D] {

  def groups(d: D): Traversable[G]

  def slices(d: D): Traversable[S]

  def valueOf(d: D, gs: (G, S)): Y

  def yRange(d: D): (Y, Y)

}

object GroupedDataView {

  final def apply[G, S, Y, D](implicit ev: GroupedDataView[G, S, Y, D]): GroupedDataView[G, S, Y, D] = ev

  implicit def mapGroupedDataView[G: Order, S: Order, Y: Plottable: AdditiveMonoid: Order]: GroupedDataView[G, S, Y, Map[(G, S), Y]] =
    new GroupedDataView[G, S, Y, Map[(G, S), Y]] {

      val yAdditiveMonoid = AdditiveMonoid[Y]
      val yPlottable = Plottable[Y]

      def groups(d: Map[(G, S), Y]): Traversable[G] =
        d.keys.map(_._1).toSet.toList.sorted // TODO cache

      def slices(d: Map[(G, S), Y]): Traversable[S] =
        d.keys.map(_._2).toSet.toList.sorted // TODO cache

      def valueOf(d: Map[(G, S), Y], gs: (G, S)): Y =
        d.get(gs).getOrElse(yAdditiveMonoid.zero)

      def yRange(d: Map[(G, S), Y]): (Y, Y) = {

        val minY = slices(d).map { s =>
          (groups(d).map { g => valueOf(d, (g, s)) } ++ List(yAdditiveMonoid.zero))
            .filter(yPlottable.isPlottable _).min
        }.min

        val maxY = slices(d).map { s =>
          (groups(d).map { g => valueOf(d, (g, s)) } ++ List(yAdditiveMonoid.zero))
            .filter(yPlottable.isPlottable _).max
        }.max

        (minY, maxY)
      }

    }

}
