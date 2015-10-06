package axle.algebra

import org.specs2.mutable.Specification

class Mean extends Specification {

  "Arithmetic Mean" should {
    "work" in {

      import spire.math.Rational
      import spire.math.RationalAlgebra
      import spire.math.RationalIsField

      val m = arithmeticMean[Rational, List[Rational]](List(Rational(1), Rational(2), Rational(3)))

      m must be equalTo Rational(2)
    }
  }

  "Harmonic Mean" should {
    "work" in {

      import spire.math.Rational
      import spire.math.RationalAlgebra
      import spire.math.RationalIsField

      val m = harmonicMean[Rational, List[Rational]](List(Rational(1), Rational(2), Rational(4)))

      m must be equalTo Rational(12, 7)
    }
  }

  "Geometric Mean" should {
    "work" in {

      import spire.math.Real
      import spire.implicits.DoubleAlgebra

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

    "1 to 625 by 2" in {

      import spire.math.Real

      val xs: List[Real] = List(1d, 5d, 25d, 125d, 625d)
      val window = 3

      val moved = movingGeometricMean[List[Real], Int, Real, List[(Real, Real)]](xs, window)

      val expected = xs.sliding(window).map(ys => geometricMean[Real, List[Real]](ys.toList)).toList

      moved must be equalTo expected
    }
  }

}