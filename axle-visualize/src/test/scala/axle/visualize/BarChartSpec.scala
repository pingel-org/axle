package axle.visualize

import org.scalatest._

import scala.Vector

import cats.implicits._
import spire.implicits.DoubleAlgebra
import spire.math.Rational
import axle.game.Dice.die
import axle.stats.Distribution0
import axle.visualize.Color.blue
import axle.web._

class BarChartSpec extends FunSuite with Matchers {

  test("BarChart render an SVG of fruit sales") {

    val sales = Map(
      "apple" -> 83.8,
      "banana" -> 77.9,
      "coconut" -> 10.1)

    val chart = BarChart[String, Double, Map[String, Double]](
      sales,
      xAxis = Some(0d),
      labelAngle = 36d *: angleDouble.degree,
      title = Some("fruit sales"))

    val filename = "fruit_sales.svg"

    svg(chart, filename)

    chart.title.get should be("fruit sales")
    new java.io.File(filename).exists should be(true)
  }

  test("BarChartGrouped render an SVG of fruit sales") {

    val fruits = Vector("apple", "banana", "coconut")

    val years = Vector(2011, 2012)

    val sales = Map(
      ("apple", 2011) -> 43.0,
      ("apple", 2012) -> 83.8,
      ("banana", 2011) -> 11.3,
      ("banana", 2012) -> 77.9,
      ("coconut", 2011) -> 88.0,
      ("coconut", 2012) -> 10.1)

    import cats.implicits._

    val chart = BarChartGrouped[String, Int, Double, Map[(String, Int), Double]](
      sales,
      // xAxis = Some(0d),
      colorOf = (slice: Int) => blue,
      title = Some("fruit sales"))

    val filename = "fruit_sales_grouped.svg"

    svg(chart, filename)

    chart.title.get should be("fruit sales")
    new java.io.File(filename).exists should be(true)
  }

  test("BarChart render a SVG of d6 + d6 probability distribution") {

    val distribution: Distribution0[Int, Rational] = for {
      a <- die(6)
      b <- die(6)
    } yield a + b

    val chart = BarChart[Int, Rational, Distribution0[Int, Rational]](
      distribution,
      xAxis = Some(Rational(0)),
      title = Some("d6 + d6"),
      labelAngle = 36d *: angleDouble.degree,
      colorOf = (i: Int) => blue,
      drawKey = false)

    val filename = "d6plusd6.svg"

    svg(chart, filename)

    chart.title.get should be("d6 + d6")
    new java.io.File(filename).exists should be(true)
  }

}
