package axle.visualize

import scala.collection.immutable.TreeMap
import scala.annotation.implicitNotFound

import axle.algebra.Plottable
import axle.algebra.Zero
import axle.stats.Distribution0
import axle.orderToOrdering
import cats.kernel.Order
import cats.implicits._

@implicitNotFound("Witness not found for PlotDataView[${X}, ${Y}, ${D}]")
trait PlotDataView[X, Y, D] {

  def xsOf(d: D): Traversable[X]

  def valueOf(d: D, x: X): Y

  def xRange(data: Seq[(String, D)], include: Option[X]): (X, X)

  def yRange(data: Seq[(String, D)], include: Option[Y]): (Y, Y)
}

object PlotDataView {

  final def apply[X, Y, D](implicit ev: PlotDataView[X, Y, D]) = ev

  implicit def treeMapDataView[X: Order: Zero: Plottable, Y: Order: Zero: Plottable]: PlotDataView[X, Y, TreeMap[X, Y]] =
    new PlotDataView[X, Y, TreeMap[X, Y]] {

      def xsOf(d: TreeMap[X, Y]): Traversable[X] = d.keys

      def valueOf(d: TreeMap[X, Y], x: X): Y = d.apply(x)

      def xRange(data: Seq[(String, TreeMap[X, Y])], include: Option[X]): (X, X) = {

        val minXCandidates = include.toList ++ (data flatMap {
          case (label, d: TreeMap[X, Y]) => xsOf(d).headOption
        })
        val minX = if (minXCandidates.size > 0) minXCandidates.min else Zero[X].zero

        val maxXCandidates = include.toList ++ (data flatMap {
          case (label, d: TreeMap[X, Y]) => xsOf(d).lastOption
        })

        val maxX = if (minXCandidates.size > 0) maxXCandidates.max else Zero[X].zero

        (minX, maxX)

      }

      def yRange(data: Seq[(String, TreeMap[X, Y])], include: Option[Y]): (Y, Y) = {

        val minYCandidates = include.toList ++ (data flatMap {
          case (label, d: TreeMap[X, Y]) =>
            val xs = xsOf(d)
            if (xs.size === 0)
              None
            else
              Some(xs map { valueOf(d, _) } min)
        }) filter { Plottable[Y].isPlottable _ }

        val minY = if (minYCandidates.size > 0) minYCandidates.min else Zero[Y].zero

        val maxYCandidates = include.toList ++ (data flatMap {
          case (label, d: TreeMap[X, Y]) => {
            val xs = xsOf(d)
            if (xs.size === 0)
              None
            else
              Some(xs map { valueOf(d, _) } max)
          }
        }) filter { Plottable[Y].isPlottable _ }

        val maxY = if (minYCandidates.size > 0) maxYCandidates.max else Zero[Y].zero

        (minY, maxY)
      }
    }

  implicit def distribution0DataView[X: Order: Zero: Plottable, Y: Order: Zero: Plottable]: PlotDataView[X, Y, Distribution0[X, Y]] =
    new PlotDataView[X, Y, Distribution0[X, Y]] {

      def xsOf(d: Distribution0[X, Y]): Traversable[X] = d.toMap.keys.toList.sorted

      def valueOf(d: Distribution0[X, Y], x: X): Y = d.probabilityOf(x)

      def xRange(data: Seq[(String, Distribution0[X, Y])], include: Option[X]): (X, X) = {

        val minXCandidates = include.toList ++ (data flatMap {
          case (label, d: Distribution0[X, Y]) => xsOf(d).headOption
        })
        val minX = if (minXCandidates.size > 0) minXCandidates.min else Zero[X].zero

        val maxXCandidates = include.toList ++ (data flatMap {
          case (label, d: Distribution0[X, Y]) => xsOf(d).lastOption
        })

        val maxX = if (minXCandidates.size > 0) maxXCandidates.max else Zero[X].zero

        (minX, maxX)

      }

      def yRange(data: Seq[(String, Distribution0[X, Y])], include: Option[Y]): (Y, Y) = {

        val minYCandidates = include.toList ++ (data flatMap {
          case (label, d: Distribution0[X, Y]) =>
            val xs = xsOf(d)
            if (xs.size === 0)
              None
            else
              Some(xs map { valueOf(d, _) } min)
        }) filter { Plottable[Y].isPlottable _ }

        val minY = if (minYCandidates.size > 0) minYCandidates.min else Zero[Y].zero

        val maxYCandidates = include.toList ++ (data flatMap {
          case (label, d: Distribution0[X, Y]) => {
            val xs = xsOf(d)
            if (xs.size === 0)
              None
            else
              Some(xs map { valueOf(d, _) } max)
          }
        }) filter { Plottable[Y].isPlottable _ }

        val maxY = if (minYCandidates.size > 0) maxYCandidates.max else Zero[Y].zero

        (minY, maxY)
      }
    }

}
