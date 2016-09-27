package axle.visualize

import org.specs2.mutable.Specification

class ScatterPlotSpec extends Specification {

  "ScatterPlot" should {
    "render a few data points" in {

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

      import spire.implicits.IntAlgebra
      val plot = ScatterPlot[Int, Int, Map[(Int, Int), Int]](data)

      import axle.web._
      val svgName = "scatter.svg"
      svg(plot, svgName)

      import axle.awt._
      val pngName = "scatter.png"
      png(plot, pngName)

      new java.io.File(svgName).exists must be equalTo true
      new java.io.File(pngName).exists must be equalTo true
    }
  }

}