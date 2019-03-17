package axle.stats

import spire.math.Rational
import org.scalatest._

class DistributionSpec extends FunSuite with Matchers {

  val prob = implicitly[ProbabilityModel[ConditionalProbabilityTable0]]
  implicit val monad = implicitly[cats.Monad[({ type λ[T] = ConditionalProbabilityTable0[T, Rational] })#λ]]

  test("Distribution map") {

    val c = ConditionalProbabilityTable0[List[Int], Rational](
      Map(
        List(1, 2, 3) -> Rational(1, 3),
        List(1, 2, 8) -> Rational(1, 2),
        List(8, 9) -> Rational(1, 6)),
      Variable("c"))

    val modelSize = monad.map(c)(_.size)

    prob.probabilityOf(modelSize, 3) should be(Rational(5, 6))
  }

}
