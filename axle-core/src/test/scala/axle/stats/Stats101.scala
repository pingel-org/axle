package axle.stats

import cats.implicits._
import org.scalatest._
import spire.algebra._
import spire.math._

class Stats101 extends FunSuite with Matchers {

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

}
