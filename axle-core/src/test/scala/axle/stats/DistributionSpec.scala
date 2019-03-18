package axle.stats

import cats.syntax.all._
import spire.math.Rational
import org.scalatest._

class DistributionSpec extends FunSuite with Matchers {

  val prob = implicitly[ProbabilityModel[ConditionalProbabilityTable0]]

  test("Distribution map") {

    type F[T] = ConditionalProbabilityTable0[T, Rational]

    val listDist: F[List[Int]] = ConditionalProbabilityTable0[List[Int], Rational](
      Map(
        List(1, 2, 3) -> Rational(1, 3),
        List(1, 2, 8) -> Rational(1, 2),
        List(8, 9) -> Rational(1, 6)),
      Variable("c"))

    val modelSize = for {
      l <- listDist
    } yield l.size

    prob.probabilityOf(modelSize, 3) should be(Rational(5, 6))
  }

}
