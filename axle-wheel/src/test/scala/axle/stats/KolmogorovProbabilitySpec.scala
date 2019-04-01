package axle.stats

import org.scalacheck.Gen
import org.scalacheck.Arbitrary

import axle.eqSymbol

class FairCoinIsKolmogorov
  extends KolmogorovProbabilityProperties(
    "Fair coin",
    Arbitrary(Gen.oneOf(List(coin()))))

import spire.math.Rational
class BiasedCoinIsKolmogorov
  extends KolmogorovProbabilityProperties(
    "Arbitrarily biased coins",
    Arbitrary(Gen.choose(0d,1d).map(Rational.apply).map(coin)))

import spire.implicits.IntAlgebra
import axle.game.Dice._

class D6IsKolmogorov
  extends KolmogorovProbabilityProperties(
    "dice",
    Arbitrary(Gen.oneOf(List(4,6,8,10,12,20).map(die))))

import spire.math.Rational

class TwoIndependentD6IsKolmogorov
  extends KolmogorovProbabilityProperties(
    "2 independent d6",
    Arbitrary(
      for {
        an <- Gen.oneOf(List(4,6,8,10,12,20))
        bn <- Gen.oneOf(List(4,6,8,10,12,20))
      } yield {
        type CPTR[T] = ConditionalProbabilityTable[T, Rational]
        import cats.syntax.all._
        for {
          a <- die(an): CPTR[Int]
          b <- die(bn): CPTR[Int]
        } yield a + b
    } ) )
