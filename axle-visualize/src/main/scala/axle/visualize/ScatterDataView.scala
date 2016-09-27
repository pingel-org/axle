package axle.visualize

trait ScatterDataView[X, Y, D] {

  def dataToDomain(data: D): Set[(X, Y)]

  def colorOf(d: D, x: X, y: Y): Color

  def xRange(data: D, include: Option[X]): (X, X)

  def yRange(data: D, include: Option[Y]): (Y, Y)

}

object ScatterDataView {

  implicit def forMap[X, Y, V](implicit v2color: V => Color): ScatterDataView[X, Y, Map[(X, Y), V]] =
    new ScatterDataView[X, Y, Map[(X, Y), V]] {

      def dataToDomain(data: Map[(X, Y), V]): Set[(X, Y)] = data.keySet

      def colorOf(data: Map[(X, Y), V], x: X, y: Y): Color = v2color(data((x, y)))
    }

  implicit def forSet[X, Y]: ScatterDataView[X, Y, Set[(X, Y)]] =
    new ScatterDataView[X, Y, Set[(X, Y)]] {

      def dataToDomain(data: Set[(X, Y)]): Set[(X, Y)] = data

      def colorOf(data: Set[(X, Y)], x: X, y: Y): Color = Color.black // TODO: make configurable
    }

}