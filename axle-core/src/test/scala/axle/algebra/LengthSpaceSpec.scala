package axle.algebra

import org.scalatest._

class LengthSpaceSpec extends FunSuite with Matchers {

  test("LengthSpace[Double, Double]") {

    axle.algebra.LengthSpace.doubleDoubleLengthSpace.onPath(0d, 10d, 0.5) should be(5d)
  }

}
