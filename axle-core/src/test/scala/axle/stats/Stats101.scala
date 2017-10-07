package axle.stats

import org.scalatest._
import spire.math._
import spire.implicits._

class Stats101 extends FunSuite with Matchers {

  test("standard deviation on a list of doubles") {

    val dist = uniformDistribution(List(2d, 4d, 4d, 4d, 5d, 5d, 7d, 9d))

    standardDeviation(dist) should be(2d)
  }

  test("standard deviation on a list of reals") {

    val dist = uniformDistribution(List[Real](
        Real(2),
        Real(4),
        Real(4),
        Real(4),
        Real(5),
        Real(5),
        Real(7),
        Real(9)))

    standardDeviation(dist) should be(Real(2))
  }

}
