package axle.stats

import org.scalacheck.Gen
import org.scalacheck.Arbitrary

import spire.math.Rational

import axle.eqSymbol
import axle.algebra.Region

import axle.stats.TestSupport._

class FairCoinIsBayes
  extends BayesTheoremProperty[Unit, ConditionalProbabilityTable, Symbol, Rational](
    "Fair coin",
    Arbitrary(Gen.oneOf(List(()))),
    u => coin(),
    m => Arbitrary(genRegion(coinSides)),
    m => Region.eqRegionIterable(coinSides))

import spire.math.Rational
class BiasedCoinIsBayes
  extends BayesTheoremProperty[Rational, ConditionalProbabilityTable, Symbol, Rational](
    "Arbitrarily biased coins",
    Arbitrary(Gen.choose(0d,1d).map(Rational.apply)),
    coin,
    bias => Arbitrary(genRegion(coinSides)),
    bias => Region.eqRegionIterable(coinSides))

import spire.implicits.IntAlgebra
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
    { case (an, bn) =>
       ProbabilityModel[ConditionalProbabilityTable].flatMap(die(an)){ a =>
         ProbabilityModel[ConditionalProbabilityTable].map(die(bn)){ b =>
           a + b           
       }}
    },
    { case (an, bn) => Arbitrary(genRegion(1 to an + bn)) },
    { case (an, bn) => Region.eqRegionIterable(1 to an + bn) }
)

import edu.uci.ics.jung.graph.DirectedSparseGraph
import axle.example.AlarmBurglaryEarthquakeBayesianNetwork
import axle.pgm.MonotypeBayesanNetwork
import cats.implicits._

class AlarmBurglaryEarthquakeBayesianNetworkIsBayes
  extends BayesTheoremProperty[
    Rational,
    ({ type L[C, W] = MonotypeBayesanNetwork[C, Boolean, W, DirectedSparseGraph] })#L,
    (Boolean, Boolean, Boolean, Boolean, Boolean),
    Rational](
    "Alarm-Burglary-Earthquake Bayesian Network",
    Arbitrary(genPortion),
    { case seed => new AlarmBurglaryEarthquakeBayesianNetwork(pEarthquake = seed).monotype },
    { case seed => Arbitrary(genRegion(AlarmBurglaryEarthquakeBayesianNetwork.domain)) },
    { case seed => Region.eqRegionIterable(AlarmBurglaryEarthquakeBayesianNetwork.domain) }
)(
    axle.pgm.MonotypeBayesanNetwork.probabilityModelForMonotypeBayesanNetwork[Boolean, DirectedSparseGraph],
    cats.kernel.Eq[(Boolean, Boolean, Boolean, Boolean, Boolean)],
    spire.algebra.Field[Rational],
    cats.kernel.Order[Rational]
)
