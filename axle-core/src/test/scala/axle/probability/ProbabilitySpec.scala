package axle.probability

import org.scalatest.funsuite._
import org.scalatest.matchers.should.Matchers

import cats.implicits._
import spire.math._
import axle.game.Dice._
import axle.probability._
import axle.algebra._
import axle.syntax.kolmogorov.kolmogorovOps
import axle.syntax.bayes.bayesOps

class ProbabilitySpec extends AnyFunSuite with Matchers {

  val monad = ConditionalProbabilityTable.monadWitness[Rational]

  val d6 = die(6)
  val d10 = die(10)

  val head = Symbol("HEAD")
  val tail = Symbol("TAIL")

  val fairCoin = ConditionalProbabilityTable[Symbol, Rational](
    Map(
      head -> Rational(1, 2),
      tail -> Rational(1, 2)))

  test("one fair coin flip") {

    fairCoin.P(RegionEq(head)) should be(Rational(1, 2))
    fairCoin.P(RegionNegate(RegionEq(head))) should be(Rational(1, 2))
    fairCoin.P(RegionEq(head) and RegionEq(tail)) should be(Rational(0))
    fairCoin.P(RegionEq(head) or RegionEq(tail)) should be(Rational(1))
  }

  test("one d6 roll") {
    d6.filter(RegionIf(_ % 4 == 1)).P(RegionEq(1)) should be(Rational(1, 2))
    d6.filter(RegionLTE(3)).P(RegionEq(1)) should be(Rational(1, 3))
  }

  test("two independent coins") {

    val bothCoinsModel = monad.flatMap(fairCoin)( c1 => monad.map(fairCoin)( c2 => (c1, c2) ))

    type TWOFLIPS = (Symbol, Symbol)

    bothCoinsModel.P(RegionIf[TWOFLIPS](_._1 == head) and RegionIf[TWOFLIPS](_._2 == head)) should be(Rational(1, 4))

    bothCoinsModel.P(RegionEq((head, head))) should be(Rational(1, 4))

    bothCoinsModel.P(RegionIf(_._1 == head)) should be(Rational(1, 2))

    bothCoinsModel.P(RegionIf[TWOFLIPS](_._1 == head) or RegionIf[TWOFLIPS](_._2 == head)) should be(Rational(3, 4))

    val coin2Conditioned = monad.map(bothCoinsModel.filter(RegionIf[TWOFLIPS](_._2 == tail)))(_._1)

    coin2Conditioned.P(RegionEq(head)) should be(Rational(1, 2))
  
 }

  test("two independent d6") {

    val bothDieModel = monad.flatMap(d6)( r1 => monad.map(d6)( r2 => (r1, r2)) )

    bothDieModel.P(RegionIf(_._1 == 1)) should be(Rational(1, 6))

    bothDieModel.P(RegionNegate(RegionIf(_._1 == 3))) should be(Rational(5, 6))

    bothDieModel.P(RegionAnd(RegionIf(_._1 == 1), RegionIf(_._2 == 2))) should be(Rational(1, 36))
  }

  test("iffy: if( heads) {d6+d6} else {d10+d10}") {

    import cats.Eq
    val headsD6D6taildD10D10 = monad.flatMap(fairCoin) { side =>
      if( Eq[Symbol].eqv(side, head) ) {
        monad.flatMap(d6) { a => monad.map(d6) { b => a + b } } 
      } else {
        monad.flatMap(d10) { a => monad.map(d10) { b => a + b } } 
      }
    }

    headsD6D6taildD10D10.P(RegionAll()) should be(Rational(1))
  }


}
