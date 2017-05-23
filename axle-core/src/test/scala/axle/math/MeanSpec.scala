package axle.math

import org.scalatest._
import axle.algebra.Bijection

class Mean extends FunSuite with Matchers {

  test("Arithmetic Mean") {

    import spire.math.Rational

    val m = arithmeticMean[Rational, List[Rational]](List(Rational(1), Rational(2), Rational(3)))

    assertResult(m)(Rational(2))
  }

  test("Harmonic Mean") {

    import spire.math.Rational

    val m = harmonicMean[Rational, List[Rational]](List(Rational(1), Rational(2), Rational(4)))

    assertResult(m)(Rational(12, 7))
  }

  test("Geometric Mean") {

    import spire.math.Real

    val m = geometricMean[Real, List[Real]](List(1d, 5d, 25d))

    assertResult(m)(5d)
  }

  test("Generalized Mean: Arithmetic Mean when p = 1") {

    import spire.implicits.DoubleAlgebra

    val xs = List(1d, 2d, 3d)

    val am = arithmeticMean[Double, List[Double]](xs)
    val gm1 = generalizedMean[Double, List[Double]](1d, xs)

    assertResult(am)(gm1)
  }

  test("Generalized Mean: Harmonic Mean when p = -1") {

    import spire.implicits.DoubleAlgebra

    val xs = List(1d, 2d, 3d)

    val hm = harmonicMean[Double, List[Double]](xs)
    val gm1 = generalizedMean[Double, List[Double]](-1d, xs)

    assertResult(hm)(gm1)
  }

  test("Generalized Mean: Geometric Mean as p approaches 0") {

    import spire.implicits.DoubleAlgebra

    val xs = List(1d, 2d, 3d)

    val geom = geometricMean[Double, List[Double]](xs)
    val gm1 = generalizedMean[Double, List[Double]](0.0001, xs)

    val diff = geom - gm1

    diff should be < 0.001
  }

  test("Generalized f-Mean: Harmonic Mean when f(x) = 1/x") {

    import spire.implicits.DoubleAlgebra

    val xs = List(1d, 2d, 3d)

    val hm = harmonicMean(xs)

    val f = new Bijection[Double, Double] {

      def apply(x: Double): Double = 1d / x

      def unapply(x: Double): Double = 1d / x
    }

    val gfm = generalizedFMean(f, xs)

    assertResult(hm)(gfm)
  }

  test("movingArithmeticMean: 1 to 100 by 5") {

    import spire.implicits.DoubleAlgebra

    val xs = (1 to 100).toList.map(_.toDouble)
    val window = 5

    val moved = movingArithmeticMean[List[Double], Int, Double, List[(Double, Double)]](xs, window)

    val expected = xs.sliding(window).map(ys => arithmeticMean(ys.toList)).toList

    assertResult(moved)(expected)
  }

  test("movingGeometricMean: 1 to 625 by 3") {

    import spire.math.Real

    val xs: List[Real] = List(1d, 5d, 25d, 125d, 625d)
    val window = 3

    val moved = movingGeometricMean[List[Real], Int, Real, List[(Real, Real)]](xs, window)

    val expected = xs.sliding(window).map(ys => geometricMean[Real, List[Real]](ys.toList)).toList

    assertResult(moved)(expected)
  }

  test("movingHarmonicMean: 1 to 5 by 3") {

    import spire.math.Real

    val xs: List[Real] = (1 to 5).toList.map(v => Real(v))
    val window = 3

    val moved = movingHarmonicMean[List[Real], Int, Real, List[(Real, Real)]](xs, window)

    val expected = xs.sliding(window).map(ys => harmonicMean[Real, List[Real]](ys.toList)).toList

    assertResult(moved)(expected)
  }

  test("movingGeneralizedMean: 1 to 5 by 3 with p=0.2") {

    import spire.implicits.DoubleAlgebra

    val xs: List[Double] = (1 to 5).toList.map(_.toDouble)
    val window = 3
    val p = 0.2

    val moved = movingGeneralizedMean[List[Double], Int, Double, List[(Double, Double)]](p, xs, window)

    val expected = xs.sliding(window).map(ys => generalizedMean[Double, List[Double]](p, ys.toList)).toList

    assertResult(moved)(expected)
  }

  test("movingGeneralizedFMean: 1 to 5 by 3 harmonicMean with f(x) = 1/x") {

    import spire.math.Real

    val xs: List[Real] = (1 to 5).toList.map(v => Real(v))
    val window = 3

    val f = new Bijection[Real, Real] {
      def apply(x: Real): Real = 1d / x
      def unapply(x: Real): Real = 1d / x
    }

    val moved = movingGeneralizedFMean[List[Real], Int, Real, List[(Real, Real)]](f, xs, window)

    val expected = xs.sliding(window).map(ys => harmonicMean[Real, List[Real]](ys.toList)).toList

    assertResult(moved)(expected)
  }

}
