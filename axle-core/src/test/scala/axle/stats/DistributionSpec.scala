package axle.stats

import spire.math.Rational
import org.scalatest._

class DistributionSpec extends FunSuite with Matchers {

  implicit val monad = Probability.monad[({ type λ[T] = ConditionalProbabilityTable0[T, Rational] })#λ, Rational]
  val prob = implicitly[Probability[({ type λ[T] = ConditionalProbabilityTable0[T, Rational] })#λ, Rational]]

  test("Distribution map") {

    val m = Map(
      List(1, 2, 3) -> Rational(1, 3),
      List(1, 2, 8) -> Rational(1, 2),
      List(8, 9) -> Rational(1, 6))

    val c = ConditionalProbabilityTable0[List[Int], Rational](m, Variable("c", m.keys.toVector))

    val modelSize = monad.map(c)(_.size)

    prob.probabilityOf(modelSize, 3) should be(Rational(5, 6))
  }

}
