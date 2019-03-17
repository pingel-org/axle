package axle.stats

import org.scalatest._

import cats.implicits._
//import cats.syntax._
import spire.math.Rational
import spire.math.sqrt
import axle.game.Dice.die
import axle.math.Σ

class StochasticLambdaCalculus extends FunSuite with Matchers {

  val prob = implicitly[ProbabilityModel[ConditionalProbabilityTable0]]
  implicit val monad = implicitly[cats.Monad[({ type λ[T] = ConditionalProbabilityTable0[T, Rational] })#λ]]

  test("iffy (stochastic if) maps fair boolean to d6 + (d6+d6)") {

    // TODO monad syntax
    val ab = monad.flatMap(die(6))(a =>
      monad.map(die(6))(b =>
        a + b))

    val distribution =
      iffy(
        binaryDecision(Rational(1, 3)),
        die(6),
        ab)

    prob.probabilityOf(distribution, 1) should be(Rational(1, 18))

    prob.probabilityOf(distribution, 12) should be(Rational(1, 54))

    Σ[Rational, IndexedSeq](distribution.values map { v => prob.probabilityOf(distribution, v) }) should be(Rational(1))
  }

  test("π estimation by testing a uniform subset of the unit square gets in the ballpark of π") {

    val n = 200

    // TODO: sample a subset of n for each of x and y
    // to compute the distribution of estimates.
    // The naive appraoch is obviously intractable
    // for all but the smallest subsets of n.
    // However, there should be a way to utilize the "if" statement to
    // reduce the complexity.

    val xDist = uniformDistribution(0 to n, Variable[Int]("x"))
    val yDist = uniformDistribution(0 to n, Variable[Int]("y"))

    // TODO monad syntax
    val piDist = monad.flatMap(xDist)(x =>
      monad.map(yDist)(y =>
        if (sqrt((x * x + y * y).toDouble) <= n) 1 else 0))

    4 * prob.probabilityOf(piDist, 1) should be > Rational(3)
  }

}
