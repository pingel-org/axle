package axle.stats

import org.scalatest._
import spire.math._
import spire.implicits._

class Stats101 extends FunSuite with Matchers {

  test("standard deviation on a list of doubles") {

    val dist = uniformDistribution(List(2d, 4d, 4d, 4d, 5d, 5d, 7d, 9d), "some doubles")

    standardDeviation(dist) should be(2d)
  }

  test("standard deviation on a list of reals") {

    val dist = uniformDistribution(List[Real](2, 4, 4, 4, 5, 5, 7, 9), "some reals")

    standardDeviation(dist) should be(2)
  }

}
