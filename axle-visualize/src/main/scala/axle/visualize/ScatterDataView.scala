package axle.visualize

import scala.annotation.implicitNotFound

/**
 * TODO this typeclass is abstract enough that it now belongs in axle.algebra
 * and should be renamed.
 *
 */

@implicitNotFound("Witness not found for ScatterDataView[${X}, ${Y}, ${D}]")
trait ScatterDataView[X, Y, D] {

  def dataToDomain(data: D): Set[(X, Y)]

}

object ScatterDataView {

  implicit def forMap[X, Y, V]: ScatterDataView[X, Y, Map[(X, Y), V]] =
    new ScatterDataView[X, Y, Map[(X, Y), V]] {

      def dataToDomain(data: Map[(X, Y), V]): Set[(X, Y)] = data.keySet
    }

  implicit def forSet[X, Y]: ScatterDataView[X, Y, Set[(X, Y)]] =
    new ScatterDataView[X, Y, Set[(X, Y)]] {

      def dataToDomain(data: Set[(X, Y)]): Set[(X, Y)] = data
    }

}
