package axle.algebra

import org.scalatest._

class ZeroSpec extends FunSuite with Matchers {

  test("Zero[Double]") {
    import spire.implicits.DoubleAlgebra
    Zero[Double].zero should be(0d)
  }

  test("Zero[Int]") {
    import spire.implicits.IntAlgebra
    Zero[Int].zero should be(0)
  }

  test("Zero[Long]") {
    import spire.implicits.LongAlgebra
    Zero[Long].zero should be(0L)
  }

}
