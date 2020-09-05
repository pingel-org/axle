package axle.algebra

import org.scalatest.funsuite._
import org.scalatest.matchers.should.Matchers

class LengthSpaceSpec extends AnyFunSuite with Matchers {

  test("LengthSpace[Double, Double]") {

    axle.algebra.LengthSpace.doubleDoubleLengthSpace.onPath(0d, 10d, 0.5) should be(5d)
  }

}
