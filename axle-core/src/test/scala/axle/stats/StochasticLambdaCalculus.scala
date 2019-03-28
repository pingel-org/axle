package axle.stats

import org.scalatest._

import cats.syntax.all._

import spire.math.Rational
import spire.math.sqrt

import axle.game.Dice.die
import axle.math.Σ
import axle.syntax.probabilitymodel._

class StochasticLambdaCalculus extends FunSuite with Matchers {

  type F[T] = ConditionalProbabilityTable0[T, Rational]

  test("iffy (stochastic if) maps fair boolean to d6 + (d6+d6)") {

    val ab = for {
      a <- (die(6) : F[Int])
      b <- (die(6) : F[Int])
    } yield a + b

    val distribution =
      iffy(
        binaryDecision(Rational(1, 3)),
        die(6),
        ab)

    distribution.P(1) should be(Rational(1, 18))

    distribution.P(12) should be(Rational(1, 54))

    Σ[Rational, IndexedSeq](distribution.values map { v => distribution.P(v) }) should be(Rational(1))
  }

  test("π estimation by testing a uniform subset of the unit square gets in the ballpark of π") {

    val n = 200

    // TODO: sample a subset of n for each of x and y
    // to compute the distribution of estimates.
    // The naive appraoch is obviously intractable
    // for all but the smallest subsets of n.
    // However, there should be a way to utilize the "if" statement to
    // reduce the complexity.

    val xDist: F[Int] = uniformDistribution(0 to n, Variable[Int]("x"))
    val yDist: F[Int] = uniformDistribution(0 to n, Variable[Int]("y"))

    val piDist = for {
      x <- xDist
      y <- yDist
    } yield (if (sqrt((x * x + y * y).toDouble) <= n) 1 else 0)

    4 * piDist.P(1) should be > Rational(3)
  }

}
