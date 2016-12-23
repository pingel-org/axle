package axle.visualize

import org.scalatest._

class ScatterPlotSpec extends FunSuite with Matchers {

  test("ScatterPlot") {

    val data = Map(
      (1, 1) -> 0,
      (2, 2) -> 0,
      (3, 3) -> 0,
      (2, 1) -> 1,
      (3, 2) -> 1,
      (0, 1) -> 2,
      (0, 2) -> 2,
      (1, 3) -> 2)

    implicit def v2color(v: Int): Color = v match {
      case 0 => Color.red
      case 1 => Color.blue
      case 2 => Color.green
    }

    import cats.implicits._
    val plot = ScatterPlot[Int, Int, Map[(Int, Int), Int]](data)

    import axle.web._
    val svgName = "scatter.svg"
    svg(plot, svgName)

    import axle.awt._
    val pngName = "scatter.png"
    png(plot, pngName)

    new java.io.File(svgName).exists should be(true)
    new java.io.File(pngName).exists should be(true)
  }

}
