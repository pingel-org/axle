package axle.visualize

import scala.annotation.implicitNotFound

@implicitNotFound("Witness not found for ScatterDataView[${X}, ${Y}, ${D}]")
trait ScatterDataView[X, Y, D] {

  def dataToDomain(data: D): Set[(X, Y)]

  def colorOf(d: D, x: X, y: Y): Color

  /**
   * labelOf optionally returns a double:
   * 1. The label of the given data point
   * 2. A Boolean representing whether that label should be permanently displayed
   *    vs. just shown as a tooltip/mouseover
   */
  def labelOf(d: D, x: X, y: Y): Option[(String, Boolean)]

}

object ScatterDataView {

  implicit def forMap[X, Y, V](
    implicit v2color: V => Color,
    v2labelOpt: (X, Y, V) => Option[(String, Boolean)]): ScatterDataView[X, Y, Map[(X, Y), V]] =
    new ScatterDataView[X, Y, Map[(X, Y), V]] {

      def dataToDomain(data: Map[(X, Y), V]): Set[(X, Y)] = data.keySet

      def colorOf(data: Map[(X, Y), V], x: X, y: Y): Color =
        v2color(data((x, y)))

      def labelOf(data: Map[(X, Y), V], x: X, y: Y): Option[(String, Boolean)] =
        v2labelOpt(x, y, data((x, y)))
    }

  implicit def forSet[X, Y]: ScatterDataView[X, Y, Set[(X, Y)]] =
    new ScatterDataView[X, Y, Set[(X, Y)]] {

      def dataToDomain(data: Set[(X, Y)]): Set[(X, Y)] = data

      def colorOf(data: Set[(X, Y)], x: X, y: Y): Color =
        Color.black // TODO: make configurable

      def labelOf(data: Set[(X, Y)], x: X, y: Y): Option[(String, Boolean)] =
        None // TODO
    }

}
