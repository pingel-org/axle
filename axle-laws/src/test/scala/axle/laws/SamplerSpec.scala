package axle.laws

import org.scalacheck.Gen
import org.scalacheck.Arbitrary

import cats.implicits._

import spire.math._
import spire.math.Numeric.RationalIsNumeric

import axle.algebra.Region
import axle.probability._
import axle.laws.TestSupport._

class BernoulliSampler
  extends SamplerProperties[Rational, ConditionalProbabilityTable, Int, Rational](
    "Bernoulli",
    Arbitrary(genPortion),
    p => bernoulliDistribution(p),
    n => Vector(0, 1),
    n => 1,
    n => 1000,
    bias => Arbitrary(genRegion(Vector(0, 1))),
    bias => Region.eqRegionIterable(Vector(0, 1)))

// (
//   Kolmogorov[ConditionalProbabilityTable],
//   Sampler[ConditionalProbabilityTable],
//   Eq[Int],
//   Field[Rational],
//   Order[Rational],
//   spire.random.Dist[Rational],
//   RationalIsNumeric)

import axle.game.Dice._

class D6Sampler
  extends SamplerProperties[Int, ConditionalProbabilityTable, Int, Rational](
    "dice",
    Arbitrary(Gen.oneOf(List(4,6,8,10,12,20))),
    die,
    n => 1 to n,
    n => n / 2,
    n => n * 20,
    n => Arbitrary(genRegion(1 to n)),
    n => Region.eqRegionIterable(1 to n))

class TwoPlatonicSolidDieAddedSampler
  extends SamplerProperties[(Int, Int), ConditionalProbabilityTable, Int, Rational](
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
    { case (an, bn) => 2 to (an + bn) },
    { case (an, bn) => spire.math.max(an, bn) / 2 },
    { case (an, bn) => (an + bn) * 20},
    { case (an, bn) => Arbitrary(genRegion(1 to an + bn)) },
    { case (an, bn) => Region.eqRegionIterable(1 to an + bn) }
)
