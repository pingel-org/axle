package axle.algebra

import org.specs2.mutable.Specification

import spire.implicits.DoubleAlgebra

class ZeroSpec extends Specification {

  "Zero[Double]" should {
    "work" in {

      Zero[Double].zero must be equalTo 0d
    }
  }

}