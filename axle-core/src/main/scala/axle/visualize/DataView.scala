package axle.visualize

import scala.annotation.implicitNotFound

import cats.kernel.Order
import cats.implicits._

import spire.algebra.AdditiveMonoid
import spire.algebra.Field

import axle.algebra.RegionEq
import axle.algebra.Plottable
import axle.probability.ConditionalProbabilityTable
import axle.probability.ProbabilityModel

/**
 * implicits for Plot and BarChart
 *
 */

@implicitNotFound("Witness not found for DataView[${X}, ${Y}, ${D}]")
trait DataView[X, Y, D] {

  def keys(d: D): Iterable[X]

  def valueOf(d: D, x: X): Y

  def yRange(d: D): (Y, Y)
}

object DataView {

  final def apply[X, Y, D](implicit ev: DataView[X, Y, D]) = ev

  implicit def mapDataView[X: Order, Y: Plottable: AdditiveMonoid: Order]: DataView[X, Y, Map[X, Y]] =
    new DataView[X, Y, Map[X, Y]] {

      val yPlottable = Plottable[Y]
      val yAdditiveMonoid = AdditiveMonoid[Y]

      def keys(d: Map[X, Y]): Iterable[X] = d.keys.toList.sorted

      def valueOf(d: Map[X, Y], x: X): Y = d.get(x).getOrElse(yAdditiveMonoid.zero)

      def yRange(d: Map[X, Y]): (Y, Y) = {

        val yMin = (keys(d).map { x => valueOf(d, x) } ++ List(yAdditiveMonoid.zero)).filter(yPlottable.isPlottable _).min
        val yMax = (keys(d).map { x => valueOf(d, x) } ++ List(yAdditiveMonoid.zero)).filter(yPlottable.isPlottable _).max

        (yMin, yMax)
      }

    }

  implicit def cptDataView[X: Order, Y: Plottable: Field: Order]: DataView[X, Y, ConditionalProbabilityTable[X, Y]] =
    new DataView[X, Y, ConditionalProbabilityTable[X, Y]] {

      val prob = ProbabilityModel[ConditionalProbabilityTable]

      val yPlottable = Plottable[Y]
      val fieldY = Field[Y]

      def keys(d: ConditionalProbabilityTable[X, Y]): Iterable[X] = d.values

      def valueOf(d: ConditionalProbabilityTable[X, Y], x: X): Y = prob.probabilityOf(d)(RegionEq(x))

      def yRange(d: ConditionalProbabilityTable[X, Y]): (Y, Y) = {

        val ks = keys(d)

        val yMin = (ks.map { x => valueOf(d, x) } ++ List(fieldY.zero)).filter(yPlottable.isPlottable _).min
        val yMax = (ks.map { x => valueOf(d, x) } ++ List(fieldY.zero)).filter(yPlottable.isPlottable _).max

        (yMin, yMax)
      }

    }

}
