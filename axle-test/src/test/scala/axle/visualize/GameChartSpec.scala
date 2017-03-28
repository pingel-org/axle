package axle.visualize

import org.scalatest._

import axle.algebra.modules.doubleDoubleModule
import axle.algebra.modules.doubleRationalModule
import axle.game.Bowling.Bowlers.goodBowler
import axle.game.Bowling.stateDistribution
import axle.jung.directedGraphJung
import axle.quanta.Angle
import axle.stats.Distribution0
import edu.uci.ics.jung.graph.DirectedSparseGraph
import spire.implicits.DoubleAlgebra
import spire.implicits.IntAlgebra
import spire.math.Rational
import cats.implicits._

class GameChartSpec extends FunSuite with Matchers {

  test("BarChart of bowling probability distribution") {

    val stateD = stateDistribution(goodBowler, 4)

    val scoreD = stateD.map(_.tallied)

    implicit val ac = Angle.converterGraphK2[Double, DirectedSparseGraph]

    // test implicit conjuring:
    PlotDataView.distribution0DataView[Int, Rational]

    val chart = BarChart[Int, Rational, Distribution0[Int, Rational]](
      scoreD,
      drawKey = true,
      xAxis = Some(Rational(0)))

    val plot = Plot[Int, Rational, Distribution0[Int, Rational]](
      Vector(("", scoreD)),
      drawKey = true,
      xAxis = Some(Rational(0)))

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
