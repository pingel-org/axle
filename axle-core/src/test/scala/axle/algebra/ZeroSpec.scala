package axle.algebra

import org.specs2.mutable.Specification

class ZeroSpec extends Specification {

  "Zero[Double]" should {
    "be available" in {
      import spire.implicits.DoubleAlgebra
      Zero[Double].zero must be equalTo 0d
    }
  }

  "Zero[Int]" should {
    "be available" in {
      import spire.implicits.IntAlgebra
      Zero[Int].zero must be equalTo 0
    }
  }

  "Zero[Long]" should {
    "be available" in {
      import spire.implicits.LongAlgebra
      Zero[Long].zero must be equalTo 0L
    }
  }

}