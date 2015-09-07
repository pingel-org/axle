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

}