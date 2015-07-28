package axle.algebra

import org.specs2.mutable.Specification
import spire.math.Rational
import spire.math.RationalAlgebra
import spire.math.RationalIsField

class Mean extends Specification {

  "Harmonic Mean" should {
    "work" in {

      val m = harmonicMean[Rational, List[Rational]](List(Rational(1), Rational(2), Rational(4)))

      m must be equalTo Rational(12, 7)
    }
  }

}