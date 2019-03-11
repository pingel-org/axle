package axle.visualize

import scala.collection.immutable.TreeMap

import org.joda.time.DateTime
import org.scalatest._

import cats.implicits._

import spire.math.sin
import spire.random.Generator
import spire.random.Generator.rng
import spire.algebra.Trig

import axle.algebra.Plottable.doublePlottable
import axle.joda.dateTimeOrder
import axle.joda.dateTimePlottable
import axle.joda.dateTimeTics
import axle.joda.dateTimeDurationLengthSpace

class PlotWavesSpec extends FunSuite with Matchers {

  implicit val trigDouble: Trig[Double] = spire.implicits.DoubleAlgebra

  test("wave plot") {

    val now = new DateTime()

    def randomTimeSeries(i: Int, gen: Generator) = {
      val φ = gen.nextDouble()
      val A = gen.nextDouble()
      val ω = 0.1 / gen.nextDouble()
      ("series %d %1.2f %1.2f %1.2f".format(i, φ, A, ω),
        new TreeMap[DateTime, Double]() ++
        (0 to 100).map(t => (now.plusMinutes(2 * t) -> A * sin(ω * t + φ))).toMap)
    }

    val waves = (0 until 20).map(i => randomTimeSeries(i, rng)).toList

    // test implicit conjuring:
    PlotDataView[String, DateTime, Double, TreeMap[DateTime, Double]]
    import cats.implicits._

    import spire.algebra.AdditiveMonoid
    implicit val amd: AdditiveMonoid[Double] = spire.implicits.DoubleAlgebra

    val plot = Plot[String, DateTime, Double, TreeMap[DateTime, Double]](
      () => waves,
      connect = true,
      colorOf = _ => Color.black,
      title = Some("Random Waves"),
      xAxisLabel = Some("time (t)"),
      yAxis = Some(now),
      yAxisLabel = Some("A·sin(ω·t + φ)")).zeroXAxis

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
