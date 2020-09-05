package axle.probability

import org.scalatest.funsuite._
import org.scalatest.matchers.should.Matchers
//import cats.syntax.all._
import cats.implicits._
import spire.math.Rational
//import axle.syntax.probabilitymodel._
import axle.probability._
import axle.algebra.RegionEq

class DistributionSpec extends AnyFunSuite with Matchers {

  test("Distribution map") {

    type F[T] = ConditionalProbabilityTable[T, Rational]

    val prob = ProbabilityModel[ConditionalProbabilityTable]

    val listDist: F[List[Int]] = ConditionalProbabilityTable[List[Int], Rational](
      Map(
        List(1, 2, 3) -> Rational(1, 3),
        List(1, 2, 8) -> Rational(1, 2),
        List(8, 9) -> Rational(1, 6)))

    val modelSize = prob.map(listDist)(_.size)

    prob.probabilityOf(modelSize)(RegionEq(3)) should be(Rational(5, 6))
  }

}
