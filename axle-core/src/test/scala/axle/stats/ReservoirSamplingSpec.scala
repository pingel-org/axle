package axle.stats

import spire.math.Rational
import org.specs2.mutable._
import axle.algebra.arithmeticMean
import spire.implicits.DoubleAlgebra

class ReservoirSamplingSpec extends Specification {

  "Reservoir Sampling" should {
    "uniformly sample 10 of the first 100 integers" in {

      val sample = reservoirSampleK(10, Stream.from(1)).drop(100).head

      val mean = arithmeticMean(sample.map(_.toDouble))

      math.abs(mean - 50) must be lessThan 20
    }
  }

}