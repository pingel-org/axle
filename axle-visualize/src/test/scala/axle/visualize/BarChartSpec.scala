package axle.visualize

import org.specs2.mutable.Specification

import spire.algebra._
import spire.implicits._
import axle.algebra._

class BarChartSpec extends Specification {

  "BarChart" should {
    "work" in {

      val sales = Map(
        "apple" -> 83.8,
        "banana" -> 77.9,
        "coconut" -> 10.1)

      val chart = BarChart[String, Double, Map[String, Double]](
        sales,
        xAxis = 0d,
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

}