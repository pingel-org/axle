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

    val fairCoin = coin()

    import cats.implicits._

    val bothCoinsModel = fairCoin.flatMap( c1 => fairCoin.map( c2 => (c1, c2) ))

    bothCoinsModel.P(RegionEqTuple1of2('HEAD) and RegionEqTuple2of2('HEAD)) should be(Rational(1, 4))

    bothCoinsModel.P(RegionEq(('HEAD, 'HEAD))) should be(Rational(1, 4))

    bothCoinsModel.P(RegionEqTuple1of2('HEAD)) should be(Rational(1, 2))

    bothCoinsModel.P(RegionEqTuple1of2('HEAD) or RegionEqTuple2of2('HEAD)) should be(Rational(3, 4))

    val coin2Conditioned = prob.map(bothCoinsModel.filter(RegionEqTuple2of2('TAIL))) {
      _._1
    }

    coin2Conditioned.P(RegionEq('HEAD)) should be(Rational(1, 2))
  
 }

  test("two independent d6") {

    val d6 = die(6)
    val bothDieModel = d6.flatMap( r1 => d6.map( r2 => (r1, r2)) )

    bothDieModel.P(RegionEqTuple1of2(1)) should be(Rational(1, 6))

    bothDieModel.P(RegionNegate(RegionEqTuple1of2(3))) should be(Rational(5, 6))

    bothDieModel.P(RegionAnd(RegionEqTuple1of2(1), RegionEqTuple2of2(2))) should be(Rational(1, 36))
  }
}
