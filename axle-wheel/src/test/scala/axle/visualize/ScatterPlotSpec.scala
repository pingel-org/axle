package axle.visualize

import org.scalatest.funsuite._
import org.scalatest.matchers.should.Matchers
import cats.implicits._

class ScatterPlotSpec extends AnyFunSuite with Matchers {

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

    import axle.visualize.Color._
    val colors = Vector(red, blue, green)

    val colorer = (x: Int, y: Int) => colors(data((x, y)))

    val labeller = (x: Int, y: Int) => data.get((x, y)).map(s => (s.show, true))

    ScatterDataView.forMap[Int, Int, Map[(Int, Int), Int]]

    import cats.implicits._
    val plot = ScatterPlot[String, Int, Int, Map[(Int, Int), Int]](
      () => data,
      colorOf = colorer,
      labelOf = labeller)

    import axle.web._
    import cats.effect._
    val svgName = "scatter.svg"
    plot.svg[IO](svgName).unsafeRunSync()

    import axle.awt._
    val pngName = "scatter.png"
    plot.png[IO](pngName).unsafeRunSync()

    new java.io.File(svgName).exists should be(true)
    new java.io.File(pngName).exists should be(true)
  }

}
