package axle.stats

import org.scalacheck.Gen
import org.scalacheck.Arbitrary

import spire.math.Rational

import axle.eqSymbol
import axle.algebra.Region
import axle.algebra.RegionEq

class FairCoinIsBayes
  extends BayesTheoremProperty[Unit, ConditionalProbabilityTable, Symbol, Rational](
    "Fair coin",
    Arbitrary(Gen.oneOf(List(()))),
    u => coin(),
    m => Arbitrary(Gen.oneOf(coinSides.map(RegionEq(_)))),
    m => Region.eqRegionIterable(coinSides))

import spire.math.Rational
class BiasedCoinIsBayes
  extends BayesTheoremProperty[Rational, ConditionalProbabilityTable, Symbol, Rational](
    "Arbitrarily biased coins",
    Arbitrary(Gen.choose(0d,1d).map(Rational.apply)),
    coin,
    bias => Arbitrary(Gen.oneOf(coinSides.map(RegionEq(_)))),
    bias => Region.eqRegionIterable(coinSides))

import spire.implicits.IntAlgebra
import axle.game.Dice._

class D6IsBayes
  extends BayesTheoremProperty[Int, ConditionalProbabilityTable, Int, Rational](
    "dice",
    Arbitrary(Gen.oneOf(List(4,6,8,10,12,20))),
    die,
    n => Arbitrary(Gen.oneOf((1 to n).map(RegionEq(_)))), // TODO random expression
    n => Region.eqRegionIterable(1 to n))

class TwoPlatonicSolidDieAddedBayes
  extends BayesTheoremProperty[(Int, Int), ConditionalProbabilityTable, Int, Rational](
    "Two Random Platonic solid die added",
    Arbitrary(for {
        an <- Gen.oneOf(List(4,6,8,12,20))
        bn <- Gen.oneOf(List(4,6,8,12,20))
    } yield  (an, bn)),
    { case (an, bn) =>
       ProbabilityModel[ConditionalProbabilityTable].flatMap(die(an)){ a =>
         ProbabilityModel[ConditionalProbabilityTable].map(die(bn)){ b =>
           a + b
         }
       }
    },
    { case (an, bn) => Arbitrary(Gen.oneOf((1 to an*bn).map(RegionEq(_)))) }, // TODO random expression
    { case (an, bn) => Region.eqRegionIterable(1 to an*bn) }
)
