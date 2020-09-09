package axle.probability

import org.scalatest.funsuite._
import org.scalatest.matchers.should.Matchers

import spire.math.Rational
import spire.math.sqrt

import axle.probability._
import axle.algebra.RegionEq
import axle.game.Dice.die
import axle.math.Σ
import axle.syntax.kolmogorov.kolmogorovOps

class StochasticLambdaCalculus extends AnyFunSuite with Matchers {

  implicit val eqInt: cats.kernel.Eq[Int] = spire.implicits.IntAlgebra
  val mcpt = ConditionalProbabilityTable.monadWitness[Rational]

  test("stochastic if maps fair boolean to d6 + (d6+d6)") {

    val ab = mcpt.flatMap(die(6)) { a =>
      mcpt.map(die(6)) { b =>
        a + b
      }
    }

    // "iffy" construction
    val distribution =
      mcpt.flatMap(binaryDecision(Rational(1, 3))) { cond =>
        if( cond ) {
          die(6)
        } else {
          ab
        }
    }

    distribution.P(RegionEq(1)) should be(Rational(1, 18))

    distribution.P(RegionEq(12)) should be(Rational(1, 54))

    Σ[Rational, IndexedSeq](distribution.values.toVector map { v => distribution.P(RegionEq(v)) }) should be(Rational(1))
  }

  test("π estimation by testing a uniform subset of the unit square gets in the ballpark of π") {

    val n = 200

    // TODO: sample a subset of n for each of x and y
    // to compute the distribution of estimates.
    // The naive appraoch is obviously intractable
    // for all but the smallest subsets of n.
    // However, there should be a way to utilize the "if" statement to
    // reduce the complexity.

    implicit val eqInt = cats.kernel.Eq.fromUniversalEquals[Int]
    
    val ints = uniformDistribution(0 to n)

    val piDist = mcpt.flatMap(ints) { x =>
      mcpt.map(ints) { y =>
        (if (sqrt((x * x + y * y).toDouble) <= n) 1 else 0)
      }
    }

    4 * piDist.P(RegionEq(1)) should be > Rational(3)
  }

}
