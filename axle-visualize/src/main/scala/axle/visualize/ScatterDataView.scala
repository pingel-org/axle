package axle.visualize

import spire.compat.ordering
import spire.algebra.Order
import axle.algebra.Zero

trait ScatterDataView[X, Y, D] {

  def dataToDomain(data: D): Set[(X, Y)]

  def colorOf(d: D, x: X, y: Y): Color

  def xRange(data: D, include: Option[X]): (X, X)

  def yRange(data: D, include: Option[Y]): (Y, Y)

}

object ScatterDataView {

  implicit def forMap[X: Order: Zero, Y: Order: Zero, V](implicit v2color: V => Color): ScatterDataView[X, Y, Map[(X, Y), V]] =
    new ScatterDataView[X, Y, Map[(X, Y), V]] {

      def dataToDomain(data: Map[(X, Y), V]): Set[(X, Y)] = data.keySet

      def colorOf(data: Map[(X, Y), V], x: X, y: Y): Color = v2color(data((x, y)))

      def xRange(data: Map[(X, Y), V], include: Option[X]): (X, X) = {

        val xs = include.toList ++ data.keySet.map(_._1)

        val minX = if (xs.size > 0) xs.min else Zero[X].zero

        val maxX = if (xs.size > 0) xs.max else Zero[X].zero

        (minX, maxX)
      }

      def yRange(data: Map[(X, Y), V], include: Option[Y]): (Y, Y) = {

        val ys = include.toList ++ data.keySet.map(_._2)

        val minY = if (ys.size > 0) ys.min else Zero[Y].zero

        val maxY = if (ys.size > 0) ys.max else Zero[Y].zero

        (minY, maxY)
      }

    }

  implicit def forSet[X: Order: Zero, Y: Order: Zero]: ScatterDataView[X, Y, Set[(X, Y)]] =
    new ScatterDataView[X, Y, Set[(X, Y)]] {

      def dataToDomain(data: Set[(X, Y)]): Set[(X, Y)] = data

      def colorOf(data: Set[(X, Y)], x: X, y: Y): Color = Color.black // TODO: make configurable

      def xRange(data: Set[(X, Y)], include: Option[X]): (X, X) = {

        val xs = include.toList ++ data.map(_._1)

        val minX = if (xs.size > 0) xs.min else Zero[X].zero

        val maxX = if (xs.size > 0) xs.max else Zero[X].zero

        (minX, maxX)
      }

      def yRange(data: Set[(X, Y)], include: Option[Y]): (Y, Y) = {

        val ys = include.toList ++ data.map(_._2)

        val minY = if (ys.size > 0) ys.min else Zero[Y].zero

        val maxY = if (ys.size > 0) ys.max else Zero[Y].zero

        (minY, maxY)
      }

    }

}