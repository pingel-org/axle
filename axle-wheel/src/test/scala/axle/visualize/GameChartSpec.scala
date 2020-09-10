package axle.visualize

import org.scalatest.funsuite._
import org.scalatest.matchers.should.Matchers

import cats.implicits._

import spire.algebra._
import spire.math.Rational

import axle.game.Bowling.Bowlers.goodBowler
import axle.game.Bowling._
import axle.probability._

class GameChartSpec extends AnyFunSuite with Matchers {

  test("BarChart of bowling probability distribution") {

    val stateD: CPTR[State] = stateDistribution(goodBowler, 4)

    val scoreD = stateD.map(_.tallied)

    // implicit val ac = Angle.converterGraphK2[Double, DirectedSparseGraph]

    // test implicit conjuring:

    implicit val dvInt = DataView.cptDataView[Int, Rational]

    val chart = BarChart[Int, Rational, ConditionalProbabilityTable[Int, Rational], String](
      () => scoreD,
      drawKey = true,
      xAxis = Some(Rational(0)))

    implicit val amInt: AdditiveMonoid[Int] = spire.implicits.IntAlgebra
    implicit val dvString = PlotDataView.cptDataView[String, Int, Rational]

    val plot = Plot[String, Int, Rational, ConditionalProbabilityTable[Int, Rational]](
      () => Vector(("", scoreD)),
      colorOf = _ => Color.black,
      drawKey = true).zeroXAxis

    import axle.web._
    import axle.awt._
    import cats.effect._

    val svgPlotName = "bowl_plot.svg"
    plot.svg[IO](svgPlotName).unsafeRunSync()

    val svgName = "bowl.svg"
    chart.svg[IO](svgName).unsafeRunSync()

    val pngName = "bowl.png"
    chart.png[IO](pngName).unsafeRunSync()

    val jpegName = "bowl.jpg"
    chart.jpeg[IO](jpegName).unsafeRunSync()

    val gifName = "bowl.gif"
    chart.gif[IO](gifName).unsafeRunSync()

    val bmpName = "bowl.bmp"
    chart.bmp[IO](bmpName).unsafeRunSync()

    val htmlName = "bowl.html"
    chart.html[IO](htmlName).unsafeRunSync()

    new java.io.File(svgPlotName).exists should be(true)
    new java.io.File(svgName).exists should be(true)
    new java.io.File(pngName).exists should be(true)
    new java.io.File(jpegName).exists should be(true)
    new java.io.File(gifName).exists should be(true)
    new java.io.File(bmpName).exists should be(true)
    new java.io.File(htmlName).exists should be(true)
  }

}
