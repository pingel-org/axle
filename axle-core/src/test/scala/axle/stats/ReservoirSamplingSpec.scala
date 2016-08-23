package axle.stats

import org.specs2.mutable._
import axle.algebra.arithmeticMean
import spire.implicits.DoubleAlgebra

class ReservoirSamplingSpec extends Specification {

  "Reservoir Sampling" should {
    "uniformly sample 15 of the first 100 integers" in {

      val sample = reservoirSampleK(15, Stream.from(1)).drop(100).head

      val mean = arithmeticMean(sample.map(_.toDouble))

      math.abs(mean - 50) must be lessThan 20
    }
  }

}