package axle.stats

import cats.implicits._
import org.scalatest.funsuite._
import org.scalatest.matchers.should.Matchers
import spire.algebra._
import spire.math._
import axle.probability._

class Stats101 extends AnyFunSuite with Matchers {

  implicit val fieldDouble: Field[Double] = spire.implicits.DoubleAlgebra
  implicit val nrootDouble: NRoot[Double] = spire.implicits.DoubleAlgebra

  test("standard deviation on a list of doubles") {

    val model = uniformDistribution(List(2d, 4d, 4d, 4d, 5d, 5d, 7d, 9d))

    standardDeviation[Double, Rational](model) should be(2d)
  }

  test("standard deviation on a list of reals") {

    val dist = uniformDistribution(List[Real](
      Real(2),
      Real(4),
      Real(4),
      Real(4),
      Real(5),
      Real(5),
      Real(7),
      Real(9)))

    standardDeviation[Real, Rational](dist) should be(Real(2))
  }

  test("bernoulliDistribution expectation (mean)") {

    val dist = bernoulliDistribution(Rational(1, 4))

    expectation[Rational, Rational](dist.events.map{Rational.apply}) should be(Rational(1, 4))
  }

}
