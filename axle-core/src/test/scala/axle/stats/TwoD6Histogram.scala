package axle.stats

import org.scalatest._

import spire.algebra._

import axle.enrichGenSeq
import axle.game.Dice.die

import spire.math.Rational

class TwoD6Histogram extends FunSuite with Matchers {

  implicit val intRing: Ring[Int] = spire.implicits.IntAlgebra

  val prob = implicitly[ProbabilityModel[ConditionalProbabilityTable0]]

  test("tally") {

    implicit val dist = axle.stats.rationalProbabilityDist

    val seed = spire.random.Seed(42)
    val gen = spire.random.Random.generatorFromSeed(seed)
    val d6a = die(6)
    val d6b = die(6)
    val rolls = (0 until 1000) map { i => prob.observe(d6a, gen) + prob.observe(d6b, gen) }

    val hist = rolls.tally
    hist.size should be(11)
  }

  test("distribution monad: combine 2 D6 correctly") {

    import cats.syntax.all._
    type F[T] = ConditionalProbabilityTable0[T, Rational]

    val twoDiceSummed = for {
      a <- die(6) : F[Int]
      b <- die(6) : F[Int]
    } yield a + b

    prob.probabilityOf(twoDiceSummed, 2) should be(Rational(1, 36))
    prob.probabilityOf(twoDiceSummed, 7) should be(Rational(1, 6))
    prob.probabilityOf(twoDiceSummed, 12) should be(Rational(1, 36))
  }

}
