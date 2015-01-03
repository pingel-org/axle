package axle.ml

import org.specs2.mutable._
import RankedClassifierPerformance._

/**
 * Test cases from https://github.com/benhamner/Metrics
 *
 */

class RankedClassifierPerformanceSpec extends Specification {

  "Average Precision" should {
    "work" in {

      averagePrecisionAtK(1 until 6, List(6, 4, 7, 1, 2), 2) must be equalTo 0.25

      averagePrecisionAtK(1 until 6, List(1, 1, 1, 1, 1), 5) must be equalTo 0.2

      averagePrecisionAtK(1 until 100, (1 until 21) ++ (200 until 600), 1) must be equalTo 1.0
    }
  }

  "Mean Average Precision" should {
    "work" in {

      meanAveragePrecisionAtK(List(1 until 5), List(1 until 5), 3) must be equalTo 1.0

      meanAveragePrecisionAtK(List(List(1, 3, 4), List(1, 2, 4), List(1, 3)), List(1 until 6, 1 until 6, 1 until 6), 3) must be equalTo 0.6851851851851851

      meanAveragePrecisionAtK(List(1 until 6, 1 until 6), List(List(6, 4, 7, 1, 2), List(1, 1, 1, 1, 1)), 5) must be equalTo 0.26

      meanAveragePrecisionAtK(List(List(1, 3), List(1, 2, 3), List(1, 2, 3)), List(1 until 6, List(1, 1, 1), List(1, 2, 1)), 3) must be equalTo (11d / 18)
    }
  }

}