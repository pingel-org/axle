package axle.visualize

import java.awt.Color.blue

import scala.Vector

import org.specs2.mutable.Specification

import axle.algebra.DirectedGraph
import axle.game.Dice.die
import axle.jung.JungDirectedGraph
import axle.quanta.Angle
import axle.stats.Distribution0
import spire.algebra.Eq
import spire.algebra.Field
import spire.implicits.DoubleAlgebra
import spire.math.Rational

class BarChartSpec extends Specification {

  "BarChart" should {
    "work" in {

      val sales = Map(
        "apple" -> 83.8,
        "banana" -> 77.9,
        "coconut" -> 10.1)

      import axle.orderStrings

      val chart = BarChart[String, Double, Map[String, Double]](
        sales,
        xAxis = 0d,
        labelAngle = 36d *: angleDouble.degree,
        title = Some("fruit sales"))

      1 must be equalTo 1
    }
  }

  "BarChartGrouped" should {
    "work" in {

      val fruits = Vector("apple", "banana", "coconut")

      val years = Vector(2011, 2012)

      val sales = Map(
        ("apple", 2011) -> 43.0,
        ("apple", 2012) -> 83.8,
        ("banana", 2011) -> 11.3,
        ("banana", 2012) -> 77.9,
        ("coconut", 2011) -> 88.0,
        ("coconut", 2012) -> 10.1)

      val chart = BarChartGrouped[String, Int, Double, Map[(String, Int), Double]](
        sales,
        xAxis = 0d,
        title = Some("fruit sales"))

      1 must be equalTo 1
    }
  }

  "BarChart" should {
    "chart d6 + d6 probability distribution" in {

      val distribution: Distribution0[Int, Rational] = for {
        a <- die(6)
        b <- die(6)
      } yield a + b

      //      DataView[Int, Rational, Distribution0[Int, Rational]]
      //      Show[Int]
      //      Plottable[Rational]
      //      Field[Double]
      //      Eq[Double]
      //      DirectedGraph[JungDirectedGraph]
      //      Module[Double, Double]
      //      Module[Double, Rational]

      import spire.implicits.IntAlgebra
      val chart = BarChart[Int, Rational, Distribution0[Int, Rational]](
        distribution,
        xAxis = Rational(0),
        title = Some("d6 + d6"),
        labelAngle = 36d *: angleDouble.degree,
        colors = List(blue),
        drawKey = false)

      1 must be equalTo 1

    }
  }

}