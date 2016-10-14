package axle

import org.specs2.mutable._

class PackageSpec extends Specification {

  "monte carlos pi" should {
    "be at least 3" in {
      import spire.implicits._
      monteCarloPiEstimate(
        (1 to 1000).toList,
        (n: Int) => n.toDouble) must be greaterThan 3d
    }
  }

  "Wallis pi" should {
    "be at least 3" in {
      wallisΠ(100).toDouble must be greaterThan 3d
    }
  }

  "fibonacci" should {
    "iteratively arrive at fib(7) == 21" in {
      fib(7) must be equalTo 21
    }
    "and recursively" in {
      recfib(7) must be equalTo 21
    }
  }

  "ackerman" should {
    "ackermann(2, 2) == " in {
      ackermann(2, 2) must be equalTo 7
    }
  }

  "mandelbrot" should {
    "work at 1.8 1.7" in {
      import spire.implicits.DoubleAlgebra
      inMandelbrotSetAt(4d, 1.8, 1.7, 100).get must be equalTo 0
    }
  }

  "intersperse" should {
    "intersperse" in {
      intersperse(7)((11 to 13).toList) must be equalTo List(11, 7, 12, 7, 13)
    }
  }
}