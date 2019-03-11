package axle.stats

import org.scalatest._

import spire.algebra._
import spire.random.Generator.rng

import axle.math.arithmeticMean

class ReservoirSamplingSpec extends FunSuite with Matchers {

  implicit val fieldDouble: Field[Double] = spire.implicits.DoubleAlgebra
  implicit val nrootDouble: NRoot[Double] = spire.implicits.DoubleAlgebra

  test("Reservoir Sampling uniformly samples 15 of the first 100 integers") {

    val sample = reservoirSampleK(15, Stream.from(1), rng).drop(100).head

    val mean = arithmeticMean(sample.map(_.toDouble))

    math.abs(mean - 50d) should be < 23d
  }

}
