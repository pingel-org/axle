package axle.algebra

import org.specs2.mutable.Specification

class Mean extends Specification {

  "Arithmetic Mean" should {
    "work" in {

      import spire.math.Rational

      val m = arithmeticMean[Rational, List[Rational]](List(Rational(1), Rational(2), Rational(3)))

      m must be equalTo Rational(2)
    }
  }

  "Harmonic Mean" should {
    "work" in {

      import spire.math.Rational

      val m = harmonicMean[Rational, List[Rational]](List(Rational(1), Rational(2), Rational(4)))

      m must be equalTo Rational(12, 7)
    }
  }

  "Geometric Mean" should {
    "work" in {

      import spire.math.Real

      val m = geometricMean[Real, List[Real]](List(1d, 5d, 25d))

      m must be equalTo 5d
    }
  }

  "Generalized Mean" should {

    "be Arithmetic Mean when p = 1" in {

      import spire.implicits.DoubleAlgebra

      val xs = List(1d, 2d, 3d)

      val am = arithmeticMean[Double, List[Double]](xs)
      val gm1 = generalizedMean[Double, List[Double]](1d, xs)

      am must be equalTo gm1
    }

    "be Harmonic Mean when p = -1" in {

      import spire.implicits.DoubleAlgebra

      val xs = List(1d, 2d, 3d)

      val hm = harmonicMean[Double, List[Double]](xs)
      val gm1 = generalizedMean[Double, List[Double]](-1d, xs)

      hm must be equalTo gm1
    }

    "be Geometric Mean as p approaches 0" in {

      import spire.implicits.DoubleAlgebra

      val xs = List(1d, 2d, 3d)

      val geom = geometricMean[Double, List[Double]](xs)
      val gm1 = generalizedMean[Double, List[Double]](0.0001, xs)

      val diff = geom - gm1

      diff must be lessThan 0.001
    }
  }

  "Generalized f-Mean" should {

    "be Harmonic Mean when f(x) = 1/x" in {

      import spire.implicits.DoubleAlgebra

      val xs = List(1d, 2d, 3d)

      val hm = harmonicMean(xs)

      val f = new Bijection[Double, Double] {

        def apply(x: Double): Double = 1d / x

        def unapply(x: Double): Double = 1d / x
      }

      val gfm = generalizedFMean(f, xs)

      hm must be equalTo gfm
    }
  }

  "movingArithmeticMean" should {

    "1 to 100 by 5" in {

      import spire.implicits.DoubleAlgebra

      val xs = (1 to 100).toList.map(_.toDouble)
      val window = 5

      val moved = movingArithmeticMean[List[Double], Int, Double, List[(Double, Double)]](xs, window)

      val expected = xs.sliding(window).map(ys => arithmeticMean(ys.toList)).toList

      moved must be equalTo expected
    }
  }

  "movingGeometricMean" should {

    "1 to 625 by 3" in {

      import spire.math.Real

      val xs: List[Real] = List(1d, 5d, 25d, 125d, 625d)
      val window = 3

      val moved = movingGeometricMean[List[Real], Int, Real, List[(Real, Real)]](xs, window)

      val expected = xs.sliding(window).map(ys => geometricMean[Real, List[Real]](ys.toList)).toList

      moved must be equalTo expected
    }
  }

  "movingHarmonicMean" should {

    "1 to 5 by 3" in {

      import spire.math.Real

      val xs: List[Real] = (1 to 5).toList.map(v => Real(v))
      val window = 3

      val moved = movingHarmonicMean[List[Real], Int, Real, List[(Real, Real)]](xs, window)

      val expected = xs.sliding(window).map(ys => harmonicMean[Real, List[Real]](ys.toList)).toList

      moved must be equalTo expected
    }
  }

  "movingGeneralizedMean" should {

    "1 to 5 by 3 with p=0.2" in {

      import spire.implicits.DoubleAlgebra

      val xs: List[Double] = (1 to 5).toList.map(_.toDouble)
      val window = 3
      val p = 0.2

      val moved = movingGeneralizedMean[List[Double], Int, Double, List[(Double, Double)]](p, xs, window)

      val expected = xs.sliding(window).map(ys => generalizedMean[Double, List[Double]](p, ys.toList)).toList

      moved must be equalTo expected
    }
  }

  "movingGeneralizedFMean" should {

    "1 to 5 by 3 harmonicMean with f(x) = 1/x" in {

      import spire.math.Real

      val xs: List[Real] = (1 to 5).toList.map(v => Real(v))
      val window = 3

      val f = new Bijection[Real, Real] {
        def apply(x: Real): Real = 1d / x
        def unapply(x: Real): Real = 1d / x
      }

      val moved = movingGeneralizedFMean[List[Real], Int, Real, List[(Real, Real)]](f, xs, window)

      val expected = xs.sliding(window).map(ys => harmonicMean[Real, List[Real]](ys.toList)).toList

      moved must be equalTo expected
    }
  }

}