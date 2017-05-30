package axle.ml

import org.scalatest._
import spire.math.Rational
import RankedClassifierPerformance._

/**
 * Test cases from https://github.com/benhamner/Metrics
 *
 */

class RankedClassifierPerformanceSpec extends FunSuite with Matchers {

  test("Average Precision") {

    averagePrecisionAtK[Int, Rational](1 until 6, List(6, 4, 7, 1, 2), 2) should be(Rational(1, 4))

    averagePrecisionAtK[Int, Rational](1 until 6, List(1, 1, 1, 1, 1), 5) should be(Rational(1, 5))

    averagePrecisionAtK[Int, Rational](1 until 100, (1 until 21) ++ (200 until 600), 1) should be(Rational.one)
  }

  test("Mean Average Precision") {

    meanAveragePrecisionAtK[Int, Rational](List(1 until 5), List(1 until 5), 3) should be(Rational.one)

    meanAveragePrecisionAtK[Int, Rational](List(List(1, 3, 4), List(1, 2, 4), List(1, 3)), List(1 until 6, 1 until 6, 1 until 6), 3) should be(Rational(37, 54)) // 0.6851851851851851

    meanAveragePrecisionAtK[Int, Rational](List(1 until 6, 1 until 6), List(List(6, 4, 7, 1, 2), List(1, 1, 1, 1, 1)), 5) should be(Rational(13, 50))

    meanAveragePrecisionAtK[Int, Rational](List(List(1, 3), List(1, 2, 3), List(1, 2, 3)), List(1 until 6, List(1, 1, 1), List(1, 2, 1)), 3) should be(Rational(11, 18)) // 0.611111111111111
  }

}
