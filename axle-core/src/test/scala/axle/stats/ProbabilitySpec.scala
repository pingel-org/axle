package axle.stats

import org.scalatest._

import cats.implicits._
import spire.math._
import axle.game.Dice._
import axle.syntax.probabilitymodel._
import axle.algebra._

class ProbabilitySpec extends FunSuite with Matchers {

  implicit val prob = ProbabilityModel[ConditionalProbabilityTable]
  // implicit val rat = new spire.math.RationalAlgebra()

  test("two independent coins") {

    val fairCoin = ConditionalProbabilityTable[Symbol, Rational](
      Map(
        'HEAD -> Rational(1, 2),
        'TAIL -> Rational(1, 2)))

    import cats.implicits._

    val bothCoinsModel = fairCoin.flatMap( c1 => fairCoin.map( c2 => (c1, c2) ))

    type TWOFLIPS = (Symbol, Symbol)

    bothCoinsModel.P(RegionIf[TWOFLIPS](_._1 == 'HEAD) and RegionIf[TWOFLIPS](_._2 == 'HEAD)) should be(Rational(1, 4))

    bothCoinsModel.P(RegionEq(('HEAD, 'HEAD))) should be(Rational(1, 4))

    bothCoinsModel.P(RegionIf[TWOFLIPS](_._1 == 'HEAD)) should be(Rational(1, 2))

    bothCoinsModel.P(RegionIf[TWOFLIPS](_._1 == 'HEAD) or RegionIf[TWOFLIPS](_._2 == 'HEAD)) should be(Rational(3, 4))

    val coin2Conditioned = bothCoinsModel.filter(RegionIf[TWOFLIPS](_._2 == 'TAIL)).map(_._1)

    coin2Conditioned.P(RegionEq('HEAD)) should be(Rational(1, 2))
  
 }

  test("two independent d6") {

    val d6 = die(6)
    val bothDieModel = d6.flatMap( r1 => d6.map( r2 => (r1, r2)) )

    bothDieModel.P(RegionIf(_._1 == 1)) should be(Rational(1, 6))

    bothDieModel.P(RegionNegate(RegionIf(_._1 == 3))) should be(Rational(5, 6))

    bothDieModel.P(RegionAnd(RegionIf(_._1 == 1), RegionIf(_._2 == 2))) should be(Rational(1, 36))
  }
}
