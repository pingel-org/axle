package axle.math

import org.scalatest._
import cats.implicits._

class MathPackageSpec extends FunSuite with Matchers {

  test("monte carlo pi be at least 2.9") {
    import spire.algebra.Field
    implicit val fieldDouble: Field[Double] = spire.implicits.DoubleAlgebra
    monteCarloPiEstimate(
      (1 to 1000).toList,
      (n: Int) => n.toDouble) should be > 2.9
  }

  test("Wallis pi > 3d") {
    wallisΠ(100).toDouble should be > 3d
  }

  test("fibonacci iteratively arrive at fib(7) == 21") {
    assertResult(fibonacciByFold(7))(21)
  }

  test("fibonacci recursively") {
    assertResult(fibonacciRecursively(7))(21)
  }

  test("exponentiation by recursive squaring") {
    import spire.algebra.EuclideanRing
    implicit val ern: EuclideanRing[Int] = spire.implicits.IntAlgebra
    assertResult(exponentiateByRecursiveSquaring(2, 10))(1024)
  }

  test("ackermann(2, 2) == ") {
    assertResult(ackermann(2, 2))(7)
  }

  test("mandelbrot at 1.8 1.7") {
    import spire.algebra.Field
    implicit val fieldDouble: Field[Double] = spire.implicits.DoubleAlgebra
    assertResult(inMandelbrotSetAt(4d, 1.8, 1.7, 100).get)(0)
  }

  test("primes using sieveOfEratosthenes") {
    // See https://primes.utm.edu/howmany.html
    val primes = sieveOfEratosthenes(10000)
    primes.filter(_ < 10).length should be(4)
    primes.filter(_ < 100).length should be(25)
    primes.filter(_ < 1000).length should be(168)
    primes.filter(_ < 10000).length should be(1229)
  }

  test("primes using primeStream") {
    // See https://primes.utm.edu/howmany.html
    import spire.algebra.Ring
    implicit val ringInt: Ring[Int] = spire.implicits.IntAlgebra
    val primes = primeStream(10000).toList
    primes.filter(_ < 10).length should be(4)
    primes.filter(_ < 100).length should be(25)
    primes.filter(_ < 1000).length should be(168)
    primes.filter(_ < 10000).length should be(1229)
  }

}
