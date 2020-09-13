package axle.stats

import org.scalatest.funsuite._
import org.scalatest.matchers.should.Matchers

import spire.algebra._
import spire.math.Rational

import axle.probability._
import axle.algebra.RegionEq
import axle.game.Dice.die
import axle.syntax.sampler._
import axle.syntax.talliable.talliableOps
import axle.syntax.kolmogorov.kolmogorovOps

class TwoD6Histogram extends AnyFunSuite with Matchers {

  implicit val intRing: CRing[Int] = spire.implicits.IntAlgebra
  implicit val intEq: cats.kernel.Eq[Int] = spire.implicits.IntAlgebra

  test("tally") {

    implicit val dist = axle.probability.rationalProbabilityDist

    val seed = spire.random.Seed(42)
    val gen = spire.random.Random.generatorFromSeed(seed)
    val d6a = die(6)
    val d6b = die(6)
    val rolls = (0 until 1000) map { i => d6a.sample(gen) + d6b.sample(gen) }

    val hist = rolls.tally
    hist.size should be(11)
  }

  test("distribution monad: combine 2 D6 correctly") {

    val monad = ConditionalProbabilityTable.monadWitness[Rational]

    val twoDiceSummed = monad.flatMap(die(6)) { a =>
      monad.map(die(6)) { b =>
        a + b
      }
    }

    twoDiceSummed.P(RegionEq(2)) should be(Rational(1, 36))
    twoDiceSummed.P(RegionEq(7)) should be(Rational(1, 6))
    twoDiceSummed.P(RegionEq(12)) should be(Rational(1, 36))
  }

}
