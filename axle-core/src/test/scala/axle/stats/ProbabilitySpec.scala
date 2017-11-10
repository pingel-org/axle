package axle.stats

import org.scalatest._

//import axle.game.Dice._
import spire.math._

class ProbabilitySpec extends FunSuite with Matchers {

  implicit val monad = ProbabilityModel.monad[({ type λ[T] = ConditionalProbabilityTable0[T, Rational] })#λ, Rational]
  val prob = implicitly[ProbabilityModel[({ type λ[T] = ConditionalProbabilityTable0[T, Rational] })#λ, Rational]]

/*
  test("two independent coins") {
    val coin1: ConditionalProbabilityTable0[Symbol, Rational] = coin()
    val coin2: ConditionalProbabilityTable0[Symbol, Rational] = coin()

    val bothCoinsModel: ConditionalProbabilityTable0[Symbol, Rational] = ??? //coin1.join(coin2)

    prob.probabilityOf(coin1, 'HEAD) should be(Rational(1, 2))
    prob.condition(coin1, CaseIs(coin2.variable)).probabilityOf(coin1, 'HEAD) should be(Rational(1, 2))

    P(coin1 is 'HEAD).apply() should be(Rational(1, 2))
    P((coin1 is 'HEAD) and (coin2 is 'HEAD)).apply() should be(Rational(1, 4))
    P((coin1 is 'HEAD) or (coin2 is 'HEAD)).apply() should be(Rational(3, 4))
  }

  test("two independent d6") {

    val d6a = die(6)
    val d6b = die(6)
    P(d6a is 1).apply() should be(Rational(1, 6))
    P((d6a is 1) and (d6b is 2)).apply() should be(Rational(1, 36))
    P(d6a isnt 3).apply() should be(Rational(5, 6))
  }
*/
}
