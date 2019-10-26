package axle.visualize

import scala.collection.immutable.TreeMap
import scala.annotation.implicitNotFound

import cats.kernel.Order
import cats.implicits._

import spire.algebra.AdditiveMonoid
import spire.algebra.Field

import axle.algebra.Plottable
import axle.stats.ConditionalProbabilityTable

@implicitNotFound("Witness not found for PlotDataView[${X}, ${Y}, ${D}]")
trait PlotDataView[S, X, Y, D] {

  def xsOf(d: D): Traversable[X]

  def valueOf(d: D, x: X): Y

  def xRange(data: Seq[(S, D)], include: Option[X]): (X, X)

  def yRange(data: Seq[(S, D)], include: Option[Y]): (Y, Y)
}

object PlotDataView {

  final def apply[S, X, Y, D](implicit ev: PlotDataView[S, X, Y, D]) = ev

  /**
   * treeMapDataView
   *
   * Note: unchecked requirement that xRange and yRange argument be non-empty
   */

  implicit def treeMapDataView[S, X: Order: Plottable, Y: Order: Plottable]: PlotDataView[S, X, Y, TreeMap[X, Y]] =
    new PlotDataView[S, X, Y, TreeMap[X, Y]] {

      def xsOf(d: TreeMap[X, Y]): Traversable[X] = d.keys

      def valueOf(d: TreeMap[X, Y], x: X): Y = d.apply(x)

      def xRange(data: Seq[(S, TreeMap[X, Y])], include: Option[X]): (X, X) = {

        val minXCandidates = include.toList ++ (data flatMap {
          case (label, d: TreeMap[X, Y]) => xsOf(d).headOption
        })
        val minX = minXCandidates.min

        val maxXCandidates = include.toList ++ (data flatMap {
          case (label, d: TreeMap[X, Y]) => xsOf(d).lastOption
        })

        val maxX = maxXCandidates.max

        (minX, maxX)

      }

      def yRange(data: Seq[(S, TreeMap[X, Y])], include: Option[Y]): (Y, Y) = {

        val minYCandidates = include.toList ++ (data flatMap {
          case (label, d: TreeMap[X, Y]) =>
            val xs = xsOf(d)
            if (xs.size === 0)
              None
            else
              Some(xs map { valueOf(d, _) } min)
        }) filter { Plottable[Y].isPlottable _ }

        val minY = minYCandidates.min

        val maxYCandidates = include.toList ++ (data flatMap {
          case (label, d: TreeMap[X, Y]) => {
            val xs = xsOf(d)
            if (xs.size === 0)
              None
            else
              Some(xs map { valueOf(d, _) } max)
          }
        }) filter { Plottable[Y].isPlottable _ }

        val maxY = maxYCandidates.max

        (minY, maxY)
      }
    }

  //  implicit def treeMapDataViewWithZeroes[S, X: Order: AdditiveMonoid: Plottable, Y: Order: AdditiveMonoid: Plottable]: PlotDataView[S, X, Y, TreeMap[X, Y]] =
  //    new PlotDataView[S, X, Y, TreeMap[X, Y]] {
  //
  //      def xsOf(d: TreeMap[X, Y]): Traversable[X] = d.keys
  //
  //      def valueOf(d: TreeMap[X, Y], x: X): Y = d.apply(x)
  //
  //      def xRange(data: Seq[(S, TreeMap[X, Y])], include: Option[X]): (X, X) = {
  //
  //        val minXCandidates = include.toList ++ (data flatMap {
  //          case (label, d: TreeMap[X, Y]) => xsOf(d).headOption
  //        })
  //        val minX = if (minXCandidates.size > 0) minXCandidates.min else AdditiveMonoid[X].zero
  //
  //        val maxXCandidates = include.toList ++ (data flatMap {
  //          case (label, d: TreeMap[X, Y]) => xsOf(d).lastOption
  //        })
  //
  //        val maxX = if (minXCandidates.size > 0) maxXCandidates.max else AdditiveMonoid[X].zero
  //
  //        (minX, maxX)
  //
  //      }
  //
  //      def yRange(data: Seq[(S, TreeMap[X, Y])], include: Option[Y]): (Y, Y) = {
  //
  //        val minYCandidates = include.toList ++ (data flatMap {
  //          case (label, d: TreeMap[X, Y]) =>
  //            val xs = xsOf(d)
  //            if (xs.size === 0)
  //              None
  //            else
  //              Some(xs map { valueOf(d, _) } min)
  //        }) filter { Plottable[Y].isPlottable _ }
  //
  //        val minY = if (minYCandidates.size > 0) minYCandidates.min else AdditiveMonoid[Y].zero
  //
  //        val maxYCandidates = include.toList ++ (data flatMap {
  //          case (label, d: TreeMap[X, Y]) => {
  //            val xs = xsOf(d)
  //            if (xs.size === 0)
  //              None
  //            else
  //              Some(xs map { valueOf(d, _) } max)
  //          }
  //        }) filter { Plottable[Y].isPlottable _ }
  //
  //        val maxY = if (minYCandidates.size > 0) maxYCandidates.max else AdditiveMonoid[Y].zero
  //
  //        (minY, maxY)
  //      }
  //    }

  implicit def cptDataView[S, X: Order: AdditiveMonoid: Plottable, Y: Order: Field: Plottable]: PlotDataView[S, X, Y, ConditionalProbabilityTable[X, Y]] =
    new PlotDataView[S, X, Y, ConditionalProbabilityTable[X, Y]] {

      def xsOf(cpt: ConditionalProbabilityTable[X, Y]): Traversable[X] = cpt.values

      def valueOf(cpt: ConditionalProbabilityTable[X, Y], x: X): Y =
        cpt.p.get(x).getOrElse(Field[Y].zero)

      def xRange(data: Seq[(S, ConditionalProbabilityTable[X, Y])], include: Option[X]): (X, X) = {

        val minXCandidates = include.toList ++ (data flatMap {
          case (label, model) => xsOf(model).headOption
        })
        val minX = if (minXCandidates.size > 0) minXCandidates.min else AdditiveMonoid[X].zero

        val maxXCandidates = include.toList ++ (data flatMap {
          case (label, model) => xsOf(model).lastOption
        })
        val maxX = if (minXCandidates.size > 0) maxXCandidates.max else AdditiveMonoid[X].zero

        (minX, maxX)
      }

      def yRange(data: Seq[(S, ConditionalProbabilityTable[X, Y])], include: Option[Y]): (Y, Y) = {

        val minYCandidates = include.toList ++ (data flatMap {
          case (label, model) =>
            val xs = xsOf(model)
            if (xs.size === 0)
              None
            else
              Some(xs map { valueOf(model, _) } min)
        }) filter { Plottable[Y].isPlottable _ }

        val minY = if (minYCandidates.size > 0) minYCandidates.min else AdditiveMonoid[Y].zero

        val maxYCandidates = include.toList ++ (data flatMap {
          case (label, model) => {
            val xs = xsOf(model)
            if (xs.size === 0)
              None
            else
              Some(xs map { valueOf(model, _) } max)
          }
        }) filter { Plottable[Y].isPlottable _ }

        val maxY = if (minYCandidates.size > 0) maxYCandidates.max else AdditiveMonoid[Y].zero

        (minY, maxY)
      }
    }

}
