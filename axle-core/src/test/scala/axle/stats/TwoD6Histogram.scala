package axle.stats

import org.scalatest._

import axle.enrichGenSeq
import axle.game.Dice.die

import spire.implicits.IntAlgebra
import spire.math.Rational

class TwoD6Histogram extends FunSuite with Matchers {

  implicit val monad = ProbabilityModel.monad[({ type λ[T] = ConditionalProbabilityTable0[T, Rational] })#λ, Rational]
  val prob = implicitly[ProbabilityModel[({ type λ[T] = ConditionalProbabilityTable0[T, Rational] })#λ, Rational]]

  test("tally") {

    val seed = spire.random.Seed(42)
    val gen = spire.random.Random.generatorFromSeed(seed)
    val d6a = die(6)
    val d6b = die(6)
    val rolls = (0 until 1000) map { i => prob.observe(d6a, gen) + prob.observe(d6b, gen) }

    val hist = rolls.tally
    hist.size should be(11)
  }

  test("distribution monad: combine 2 D6 correctly") {

    import cats.implicits._

    val twoDiceSummed = for {
      a <- die(6)
      b <- die(6)
    } yield a + b

    prob.probabilityOf(twoDiceSummed, 2) should be(Rational(1, 36))
    prob.probabilityOf(twoDiceSummed, 7) should be(Rational(1, 6))
    prob.probabilityOf(twoDiceSummed, 12) should be(Rational(1, 36))
  }

}
