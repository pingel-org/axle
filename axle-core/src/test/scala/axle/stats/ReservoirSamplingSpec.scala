package axle.stats

import org.scalatest._
import axle.math.arithmeticMean
import spire.implicits.DoubleAlgebra
import spire.random.Generator.rng

class ReservoirSamplingSpec extends FunSuite with Matchers {

  test("Reservoir Sampling uniformly samples 15 of the first 100 integers") {

    val sample = reservoirSampleK(15, Stream.from(1), rng).drop(100).head

    val mean = arithmeticMean(sample.map(_.toDouble))

    math.abs(mean - 50d) should be < 23d
  }

}
