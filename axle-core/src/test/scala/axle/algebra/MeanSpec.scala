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

}