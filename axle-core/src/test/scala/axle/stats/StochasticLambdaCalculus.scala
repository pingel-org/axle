package axle.stats

import org.scalatest._

import cats.implicits._
//import cats.syntax._
import spire.math.Rational
import spire.math.sqrt
import axle.game.Dice.die
import axle.math.Σ

class StochasticLambdaCalculus extends FunSuite with Matchers {

  implicit val monad = ProbabilityModel.monad[({ type λ[T] = ConditionalProbabilityTable0[T, Rational] })#λ, Rational]
  val prob = implicitly[ProbabilityModel[({ type λ[T] = ConditionalProbabilityTable0[T, Rational] })#λ, Rational]]

  test("iffy (stochastic if) maps fair boolean to d6 + (d6+d6)") {

    val distribution =
      iffy[Int, Rational, ({ type λ[T] = ConditionalProbabilityTable0[T, Rational] })#λ, ({ type λ[T] = ConditionalProbabilityTable0[T, Rational] })#λ](
        binaryDecision(Rational(1, 3)),
        die(6),
        for { a <- die(6); b <- die(6) } yield a + b)

    prob.probabilityOf(distribution, 1) should be(Rational(1, 18))

    prob.probabilityOf(distribution, 12) should be(Rational(1, 54))

    Σ[Rational, IndexedSeq[Rational]](distribution.values map { v => prob.probabilityOf(distribution, v) }) should be(Rational(1))
  }

  test("π estimation by testing a uniform subset of the unit square gets in the ballpark of π") {

    val n = 200

    // TODO: sample a subset of n for each of x and y
    // to compute the distribution of estimates.
    // The naive appraoch is obviously intractable
    // for all but the smallest subsets of n.
    // However, there should be a way to utilize the "if" statement to
    // reduce the complexity.

    val piDist = for {
      x <- uniformDistribution(0 to n)
      y <- uniformDistribution(0 to n)
    } yield if (sqrt((x * x + y * y).toDouble) <= n) 1 else 0

    4 * prob.probabilityOf(piDist, 1) should be > Rational(3)
  }

}
