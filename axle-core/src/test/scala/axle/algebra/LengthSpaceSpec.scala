package axle.algebra

import org.specs2.mutable.Specification

class LengthSpaceSpec extends Specification {

  "LengthSpace[Double, Double]" should {
    "work" in {

      val lsdd = new DoubleDoubleLengthSpace {}

      lsdd.onPath(0d, 10d, 0.5) must be equalTo 5d
    }
  }

}