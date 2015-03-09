package axle.algebra

import org.specs2.mutable.Specification
import spire.math.Rational

class Mean extends Specification {

  "Harmonic Mean" should {
    "work" in {

      harmonicMean(List(Rational(1), Rational(2), Rational(4))) must be equalTo Rational(12, 7)
    }
  }

}