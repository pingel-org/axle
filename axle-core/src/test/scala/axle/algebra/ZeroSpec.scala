package axle.algebra

import org.specs2.mutable.Specification

import spire.implicits.DoubleAlgebra

class ZeroSpec extends Specification {

  "Zero[Double]" should {
    "work" in {

      val zd = implicitly[Zero[Double]]

      zd.zero must be equalTo 0d
    }
  }

}