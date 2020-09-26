package axle.laws

import org.scalacheck.Gen
import org.scalacheck.Arbitrary

import cats.implicits._
import axle.algebra.Region
import axle.probability._
import axle.laws.generator._

import spire.math.Rational
class BernoulliIsBayes
  extends BayesTheoremProperty[Rational, ConditionalProbabilityTable, Int, Rational](
    "Bernoulli",
    Arbitrary(genPortion),
    p => bernoulliDistribution(p),
    bias => Arbitrary(genRegion(Vector(0, 1))),
    bias => Region.eqRegionIterable(Vector(0, 1)))

import axle.game.Dice._

class D6IsBayes
  extends BayesTheoremProperty[Int, ConditionalProbabilityTable, Int, Rational](
    "dice",
    Arbitrary(Gen.oneOf(List(4,6,8,10,12,20))),
    die,
    n => Arbitrary(genRegion(1 to n)),
    n => Region.eqRegionIterable(1 to n))

class TwoPlatonicSolidDieAddedBayes
  extends BayesTheoremProperty[(Int, Int), ConditionalProbabilityTable, Int, Rational](
    "Two Random Platonic solid die added",
    Arbitrary(for {
        an <- Gen.oneOf(List(4,6,8,12,20))
        bn <- Gen.oneOf(List(4,6,8,12,20))
    } yield  (an, bn)),
    { case (an, bn) => {
      val monad = ConditionalProbabilityTable.monadWitness[Rational]
      monad.flatMap(die(an)) { a =>
         monad.map(die(bn)) { b =>
           a + b
        }}
      }
    },
    { case (an, bn) => Arbitrary(genRegion(1 to an + bn)) },
    { case (an, bn) => Region.eqRegionIterable(1 to an + bn) }
)
