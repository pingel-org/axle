package axle.stats

import org.scalatest._

import spire.math._
import axle.game.Dice._
import axle.syntax.probabilitymodel._
import axle.algebra._

class ProbabilitySpec extends FunSuite with Matchers {

  implicit val prob = ProbabilityModel[ConditionalProbabilityTable]
  // implicit val rat = new spire.math.RationalAlgebra()

  test("two independent coins") {

    val coin1 = coin()
    val coin2 = coin()

    import cats.implicits._

    val bothCoinsModel = prob.chain(coin1)(coin2)

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

    implicit val eqInt: cats.kernel.Eq[Int] = spire.implicits.IntAlgebra

    val bothDieModel = prob.chain(die(6))(die(6))

    bothDieModel.P(RegionEqTuple1of2(1)) should be(Rational(1, 6))

    bothDieModel.P(RegionNegate(RegionEqTuple1of2(3))) should be(Rational(5, 6))

    bothDieModel.P(
      { rolls: (Int, Int) => (rolls._1 === 1) && (rolls._2 === 2)}
    ) should be(Rational(1, 36))
  }
}
