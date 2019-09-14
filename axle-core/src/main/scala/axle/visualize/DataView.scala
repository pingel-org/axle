package axle.visualize

import scala.annotation.implicitNotFound

import cats.kernel.Order
import cats.implicits._

import spire.algebra.AdditiveMonoid
//import spire.algebra.Field

import axle.algebra.Plottable
//import axle.stats.ProbabilityModel

/**
 * implicits for Plot and BarChart
 *
 */

@implicitNotFound("Witness not found for DataView[${X}, ${Y}, ${D}]")
trait DataView[X, Y, D] {

  def keys(d: D): Traversable[X]

  def valueOf(d: D, x: X): Y

  def yRange(d: D): (Y, Y)
}

object DataView {

  final def apply[X, Y, D](implicit ev: DataView[X, Y, D]) = ev

  implicit def mapDataView[X: Order, Y: Plottable: AdditiveMonoid: Order]: DataView[X, Y, Map[X, Y]] =
    new DataView[X, Y, Map[X, Y]] {

      val yPlottable = Plottable[Y]
      val yAdditiveMonoid = AdditiveMonoid[Y]

      def keys(d: Map[X, Y]): Traversable[X] = d.keys.toList.sorted

      def valueOf(d: Map[X, Y], x: X): Y = d.get(x).getOrElse(yAdditiveMonoid.zero)

      def yRange(d: Map[X, Y]): (Y, Y) = {

        val yMin = (keys(d).map { x => valueOf(d, x) } ++ List(yAdditiveMonoid.zero)).filter(yPlottable.isPlottable _).min
        val yMax = (keys(d).map { x => valueOf(d, x) } ++ List(yAdditiveMonoid.zero)).filter(yPlottable.isPlottable _).max

        (yMin, yMax)
      }

    }

  // implicit def probabilityDataView[X: Order, Y: Plottable: Field: Order, M[_, _]](
  //   implicit
  //   prob: ProbabilityModel[M]): DataView[X, Y, M[X, Y]] =
  //   new DataView[X, Y, M[X, Y]] {

  //     val yPlottable = Plottable[Y]
  //     val fieldY = Field[Y]

  //     def keys(d: M[X, Y]): Traversable[X] = prob.regions(d)

  //     def valueOf(d: M[X, Y], x: X): Y = prob.probabilityOf(d)(x)

  //     def yRange(d: M[X, Y]): (Y, Y) = {

  //       val ks = keys(d)

  //       val yMin = (ks.map { x => valueOf(d, x) } ++ List(fieldY.zero)).filter(yPlottable.isPlottable _).min
  //       val yMax = (ks.map { x => valueOf(d, x) } ++ List(fieldY.zero)).filter(yPlottable.isPlottable _).max

  //       (yMin, yMax)
  //     }

  //   }

}
