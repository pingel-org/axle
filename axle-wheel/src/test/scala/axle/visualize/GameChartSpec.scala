package axle.visualize

import org.scalatest._

import cats.implicits._

import spire.algebra._
import spire.math.Rational

import axle.game.Bowling.Bowlers.goodBowler
import axle.game.Bowling._
import axle.stats.ConditionalProbabilityTable

class GameChartSpec extends FunSuite with Matchers {

  test("BarChart of bowling probability distribution") {

    val stateD: F[State] = stateDistribution(goodBowler, 4)

    val scoreD = for {
      state <- stateD
    } yield state.tallied

    // implicit val ac = Angle.converterGraphK2[Double, DirectedSparseGraph]

    // test implicit conjuring:

    implicit val dvInt = DataView.probabilityDataView[Int, Rational, ConditionalProbabilityTable]

    val chart = BarChart[Int, Rational, ConditionalProbabilityTable[Int, Rational], String](
      () => scoreD,
      drawKey = true,
      xAxis = Some(Rational(0)))

    implicit val amInt: AdditiveMonoid[Int] = spire.implicits.IntAlgebra
    implicit val dvString = PlotDataView.probabilityDataView[String, Int, Rational, ConditionalProbabilityTable]

    val plot = Plot[String, Int, Rational, ConditionalProbabilityTable[Int, Rational]](
      () => Vector(("", scoreD)),
      colorOf = _ => Color.black,
      drawKey = true).zeroXAxis

    import axle.web._
    import axle.awt._

    val svgPlotName = "bowl_plot.svg"
    svg(plot, svgPlotName)

    val svgName = "bowl.svg"
    svg(chart, svgName)

    val pngName = "bowl.png"
    png(chart, pngName)

    val jpegName = "bowl.jpg"
    jpeg(chart, jpegName)

    val gifName = "bowl.gif"
    gif(chart, gifName)

    val bmpName = "bowl.bmp"
    bmp(chart, bmpName)

    val htmlName = "bowl.html"
    html(chart, htmlName)

    new java.io.File(svgPlotName).exists should be(true)
    new java.io.File(svgName).exists should be(true)
    new java.io.File(pngName).exists should be(true)
    new java.io.File(jpegName).exists should be(true)
    new java.io.File(gifName).exists should be(true)
    new java.io.File(bmpName).exists should be(true)
    new java.io.File(htmlName).exists should be(true)
  }

}
