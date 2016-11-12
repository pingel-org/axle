package axle.visualize

import scala.collection.immutable.TreeMap
import scala.util.Random.nextDouble
import spire.math.sin
import spire.implicits.DoubleAlgebra

import org.joda.time.DateTime
import org.specs2.mutable.Specification

import axle.algebra.Plottable.doublePlottable
import axle.joda.dateTimeOrder
import axle.joda.dateTimePlottable
import axle.joda.dateTimeTics
import axle.joda.dateTimeDurationLengthSpace
import axle.orderToOrdering

class PlotWavesSpec extends Specification {

  "wave plot" should {
    "work" in {

      import spire.compat.ordering

      val now = new DateTime()

      def randomTimeSeries(i: Int) = {
        val φ = nextDouble
        val A = nextDouble
        val ω = 0.1 / nextDouble
        ("series %d %1.2f %1.2f %1.2f".format(i, φ, A, ω),
          new TreeMap[DateTime, Double]() ++
          (0 to 100).map(t => (now.plusMinutes(2 * t) -> A * sin(ω * t + φ))).toMap)
      }

      val waves = (0 until 20).map(randomTimeSeries).toList

      implicit val zeroDT = axle.joda.dateTimeZero(now)

      // test implicit conjuring:
      import cats.implicits._
      PlotDataView[DateTime, Double, TreeMap[DateTime, Double]]

      val plot = Plot(
        waves,
        connect = true,
        title = Some("Random Waves"),
        xAxis = Some(0d),
        xAxisLabel = Some("time (t)"),
        yAxis = Some(now),
        yAxisLabel = Some("A·sin(ω·t + φ)"))

      import axle.web._
      val svgName = "waves.svg"
      svg(plot, svgName)

      import axle.awt._
      val pngName = "waves.png"
      png(plot, pngName)

      new java.io.File(svgName).exists must be equalTo true
      new java.io.File(pngName).exists must be equalTo true
    }
  }

}
