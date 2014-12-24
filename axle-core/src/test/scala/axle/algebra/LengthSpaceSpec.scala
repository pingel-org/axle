package axle.algebra

import org.specs2.mutable.Specification

class LengthSpaceSpec extends Specification {

  "LengthSpace[Double, Double]" should {
    "work" in {

      axle.algebra.LengthSpace.doubleDoubleLengthSpace.onPath(0d, 10d, 0.5) must be equalTo 5d
    }
  }

}