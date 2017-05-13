package axle.stats

import org.scalatest._

import axle.game.Dice.die
import axle.algebra.Σ
import spire.math.Rational
import cats.implicits._

class StochasticLambdaCalculus extends FunSuite with Matchers {

  test("iffy (stochastic if) maps fair boolean to d6 + (d6+d6)") {

    val distribution =
      iffy(
        binaryDecision(Rational(1, 3)),
        die(6),
        for { a <- die(6); b <- die(6) } yield a + b)

    distribution.probabilityOf(1) should be(Rational(1, 18))

    distribution.probabilityOf(12) should be(Rational(1, 54))

    Σ[Rational, IndexedSeq[Rational]](distribution.values map distribution.probabilityOf) should be(Rational(1))
  }

  test("π estimation by testing a uniform subset of the unit square gets in the ballpark of π") {

    val n = 200

    // TODO: sample a subset of n for each of x and y
    // to compute the distribution of estimates.
    // The naive appraoch is obviously intractable
    // for all but the smallest subsets of n.
    // However, there should be a way to utilize the "if" statement to
    // reduce the complexity.

    val piDist: Distribution0[Int, Rational] = for {
      x <- uniformDistribution(0 to n, "x")
      y <- uniformDistribution(0 to n, "y")
    } yield if (spire.math.sqrt((x * x + y * y).toDouble) <= n) 1 else 0

    4 * piDist.probabilityOf(1) should be > Rational(3)
  }

}
