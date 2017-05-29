package axle.visualize

import scala.collection.immutable.TreeMap
import scala.util.Random.nextDouble

import org.joda.time.DateTime
import org.scalatest._

import cats.Order.catsKernelOrderingForOrder
import spire.math.sin
import spire.implicits.DoubleAlgebra
import axle.eqTreeMap
import axle.algebra.Plottable.doublePlottable
import axle.joda.dateTimeOrder
import axle.joda.dateTimePlottable
import axle.joda.dateTimeTics
import axle.joda.dateTimeDurationLengthSpace

class PlotWavesSpec extends FunSuite with Matchers {

  test("wave plot") {

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
    PlotDataView[String, DateTime, Double, TreeMap[DateTime, Double]]
    import cats.implicits._

    val plot = Plot[String, DateTime, Double, TreeMap[DateTime, Double]](
      () => waves,
      connect = true,
      colorOf = _ => Color.black,
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

    new java.io.File(svgName).exists should be(true)
    new java.io.File(pngName).exists should be(true)
  }

}
