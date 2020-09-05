package axle.stats

import org.scalatest.funsuite._
import org.scalatest.matchers.should.Matchers

import spire.algebra._
import spire.random.Generator.rng

import axle.math.arithmeticMean

class ReservoirSamplingSpec extends AnyFunSuite with Matchers {

  implicit val fieldDouble: Field[Double] = spire.implicits.DoubleAlgebra
  implicit val nrootDouble: NRoot[Double] = spire.implicits.DoubleAlgebra

  test("Reservoir Sampling uniformly samples 15 of the first 100 integers") {

    val sample = reservoirSampleK(15, LazyList.from(1), rng).drop(100).head

    val mean = arithmeticMean(sample.map(_.toDouble))

    math.abs(mean - 50d) should be < 23d
  }

}
