package axle.visualize

import org.scalatest.funsuite._
import org.scalatest.matchers.should.Matchers

import cats.effect._
import cats.implicits._

import spire.math.Rational
import spire.algebra._

import axle.game.Dice.die
import axle.probability._
import axle.visualize.Color.blue
import axle.web._

class BarChartSpec extends AnyFunSuite with Matchers {

  implicit val fieldDouble: Field[Double] = spire.implicits.DoubleAlgebra

  test("BarChart render an SVG of fruit sales") {

    val sales = Map(
      "apple" -> 83.8,
      "banana" -> 77.9,
      "coconut" -> 10.1)

    val chart = BarChart[String, Double, Map[String, Double], String](
      () => sales,
      title = Some("fruit sales"),
      xAxis = Some(0d),
      labelAngle = Some(36d *: angleDouble.degree),
      hoverOf = (c: String) => Some(c),
      linkOf = (c: String) => Some((new java.net.URL(s"http://wikipedia.org/wiki/$c"), Color.lightGray)))

    val filename = "fruit_sales.svg"

    chart.svg[IO](filename).unsafeRunSync()

    chart.title.get should be("fruit sales")
    new java.io.File(filename).exists should be(true)
  }

  test("BarChartGrouped render an SVG of fruit sales") {

    val sales = Map(
      ("apple", 2011) -> 43.0,
      ("apple", 2012) -> 83.8,
      ("banana", 2011) -> 11.3,
      ("banana", 2012) -> 77.9,
      ("coconut", 2011) -> 88.0,
      ("coconut", 2012) -> 10.1)

    import java.net.URL

    val chart = BarChartGrouped[String, Int, Double, Map[(String, Int), Double], String](
      () => sales,
      // xAxis = Some(0d),
      title = Some("fruit sales"),
      colorOf = (g: String, s: Int) => g match {
        case "apple"   => Color.red
        case "banana"  => Color.yellow
        case "coconut" => Color.brown
      },
      hoverOf = (g: String, s: Int) => Some(s"$g $s"),
      linkOf = (g: String, s: Int) => Some((new URL(s"http://wikipedia.org/wiki/$g"), Color.lightGray)))

    val filename = "fruit_sales_grouped.svg"

    chart.svg[IO](filename).unsafeRunSync()

    chart.title.get should be("fruit sales")
    new java.io.File(filename).exists should be(true)
  }

  test("BarChart render a SVG of d6 + d6 probability distribution") {

    val distribution = die(6).events.flatMap { a =>
      die(6).events.map { b =>
        a + b
      }
    }

    implicit val dataViewCPT: DataView[Int, Rational, ConditionalProbabilityTable[Int, Rational]] =
      DataView.cptDataView[Int, Rational]

    val chart = BarChart[Int, Rational, ConditionalProbabilityTable[Int, Rational], String](
      () => distribution,
      xAxis = Some(Rational(0)),
      title = Some("d6 + d6"),
      labelAngle = Some(36d *: angleDouble.degree),
      colorOf = (i: Int) => blue,
      drawKey = false)

    val filename = "d6plusd6.svg"

    chart.svg[IO](filename).unsafeRunSync()

    chart.title.get should be("d6 + d6")
    new java.io.File(filename).exists should be(true)
  }

}
