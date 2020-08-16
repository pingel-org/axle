package axle.stats

import org.scalacheck.Gen
import org.scalacheck.Arbitrary

import spire.math.Rational

import axle.eqSymbol
import axle.algebra.Region
import axle.algebra.RegionEq
import axle.pgm.MonotypeBayesanNetwork

class FairCoinIsKolmogorov
  extends KolmogorovProbabilityProperties[Unit, ConditionalProbabilityTable, Symbol, Rational](
    "Fair coin",
    Arbitrary(Gen.oneOf(List(()))),
    u => coin(),
    m => Arbitrary(Gen.oneOf(coinSides.map(RegionEq(_)))),
    m => Region.eqRegionIterable(coinSides))

import spire.math.Rational
class BiasedCoinIsKolmogorov
  extends KolmogorovProbabilityProperties[Rational, ConditionalProbabilityTable, Symbol, Rational](
    "Arbitrarily biased coins",
    Arbitrary(Gen.choose(0d,1d).map(Rational.apply)),
    coin,
    bias => Arbitrary(Gen.oneOf(coinSides.map(RegionEq(_)))),
    bias => Region.eqRegionIterable(coinSides))

import spire.implicits.IntAlgebra
import axle.game.Dice._

class D6IsKolmogorov
  extends KolmogorovProbabilityProperties[Int, ConditionalProbabilityTable, Int, Rational](
    "dice",
    Arbitrary(Gen.oneOf(List(4,6,8,10,12,20))),
    die,
    n => Arbitrary(Gen.oneOf((1 to n).map(RegionEq(_)))), // TODO random expression
    n => Region.eqRegionIterable(1 to n))

class TwoPlatonicSolidDieAddedKolmogorov
  extends KolmogorovProbabilityProperties[(Int, Int), ConditionalProbabilityTable, Int, Rational](
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

import edu.uci.ics.jung.graph.DirectedSparseGraph
import axle.example.AlarmBurglaryEarthquakeBayesianNetwork
import cats.implicits._

class AlarmBurglaryEarthQuakeBayesianNetworkIsKolmogorov
  extends KolmogorovProbabilityProperties[
    Rational,
    ({ type L[C, W] = MonotypeBayesanNetwork[C, Boolean, W, DirectedSparseGraph] })#L,
    (Boolean, Boolean, Boolean, Boolean, Boolean),
    Rational](
    "Alarm-Burglary-Earthquake Bayesian Network",
    // Arbitrary[T] -- arbitrary seed
    // TODO non-1 numerators
    Arbitrary(for {
      denominator <- Gen.oneOf(1 to 1000)
      numerator <- Gen.oneOf(1 to denominator)
    } yield Rational(numerator.toLong, denominator.toLong)),
    // T => M[E, V]
    { case seed => MonotypeBayesanNetwork(
        new AlarmBurglaryEarthquakeBayesianNetwork(pEarthquake = seed).bn,
        AlarmBurglaryEarthquakeBayesianNetwork.select,
        AlarmBurglaryEarthquakeBayesianNetwork.combine1,
        AlarmBurglaryEarthquakeBayesianNetwork.combine2)
    },
    // T => Arbitrary[Region[E]]
    // TODO random expression
    { case seed => Arbitrary(Gen.oneOf(AlarmBurglaryEarthquakeBayesianNetwork.domain.map(RegionEq(_)))) },
    // T => Eq[Region[E]]
    { case seed => Region.eqRegionIterable(AlarmBurglaryEarthquakeBayesianNetwork.domain) }
  )(
    axle.pgm.MonotypeBayesanNetwork.probabilityModelForMonotypeBayesanNetwork[Boolean, DirectedSparseGraph],
    cats.kernel.Eq[(Boolean, Boolean, Boolean, Boolean, Boolean)],
    spire.algebra.Field[Rational],
    cats.kernel.Order[Rational]
  )
