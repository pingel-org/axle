package axle.visualize

import scala.collection.immutable.TreeMap

import axle.algebra.Plottable
import spire.compat.ordering
import spire.implicits.IntAlgebra
import spire.implicits.eqOps

trait PlotDataView[X, Y, D] {

  def xsOf(d: D): Traversable[X]

  def valueOf(d: D, x: X): Y

  def xRange(data: Seq[(String, D)], xPlottable: Plottable[X], include: Option[X]): (X, X)

  def yRange(data: Seq[(String, D)], yPlottable: Plottable[Y], include: Option[Y]): (Y, Y)
}

object PlotDataView {

  implicit def treeMapDataView[X, Y]: PlotDataView[X, Y, TreeMap[X, Y]] =
    new PlotDataView[X, Y, TreeMap[X, Y]] {

      def xsOf(d: TreeMap[X, Y]): Traversable[X] = d.keys

      def valueOf(d: TreeMap[X, Y], x: X): Y = d.apply(x)

      def xRange(data: Seq[(String, TreeMap[X, Y])], xPlottable: Plottable[X], include: Option[X]): (X, X) = {

        implicit val order = xPlottable.order
        
        val minXCandidates = include.toList ++ (data flatMap {
          case (label, d: TreeMap[X, Y]) => xsOf(d).headOption
        })
        val minX = if (minXCandidates.size > 0) minXCandidates.min else xPlottable.zero

        val maxXCandidates = include.toList ++ (data flatMap {
          case (label, d: TreeMap[X, Y]) => xsOf(d).lastOption
        })

        val maxX = if (minXCandidates.size > 0) maxXCandidates.max else xPlottable.zero

        (minX, maxX)

      }

      def yRange(data: Seq[(String, TreeMap[X, Y])], yPlottable: Plottable[Y], include: Option[Y]): (Y, Y) = {

        implicit val order = yPlottable.order
        
        val minYCandidates = include.toList ++ (data flatMap {
          case (label, d: TreeMap[X, Y]) =>
            val xs = xsOf(d)
            if (xs.size === 0)
              None
            else
              Some(xs map { valueOf(d, _) } min)
        }) filter { yPlottable.isPlottable _ }

        val minY = if (minYCandidates.size > 0) minYCandidates.min else yPlottable.zero

        val maxYCandidates = include.toList ++ (data flatMap {
          case (label, d: TreeMap[X, Y]) => {
            val xs = xsOf(d)
            if (xs.size === 0)
              None
            else
              Some(xs map { valueOf(d, _) } max)
          }
        }) filter { yPlottable.isPlottable _ }

        val maxY = if (minYCandidates.size > 0) maxYCandidates.max else yPlottable.zero

        (minY, maxY)
      }
    }
}