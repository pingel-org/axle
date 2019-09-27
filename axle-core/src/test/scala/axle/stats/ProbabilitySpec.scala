package axle.stats

import org.scalatest._

import spire.math._
import axle.game.Dice._
import axle.syntax.probabilitymodel._
import axle.algebra.RegionEq
import axle.algebra.RegionEqTuple2of2

class ProbabilitySpec extends FunSuite with Matchers {

  implicit val prob = ProbabilityModel[ConditionalProbabilityTable]

  test("two independent coins") {

    val coin1 = coin()
    val coin2 = coin()

    import cats.implicits._
    //import cats.syntax.all._

    val bothCoinsModel = prob.chain(coin1)(coin2)

    bothCoinsModel.P(
      { coins: (Symbol, Symbol) => (coins._1 === 'HEAD) && (coins._2 === 'HEAD)}
    ) should be(Rational(1, 4))

    // implicit val rat = new spire.math.RationalAlgebra()

    bothCoinsModel.P(RegionEq(('HEAD, 'HEAD))) should be(Rational(1, 4))

    bothCoinsModel.P(
      { coins: (Symbol, Symbol) => coins._1 === 'HEAD }
    ) should be(Rational(1, 2))

    bothCoinsModel.P(
      { coins: (Symbol, Symbol) => (coins._1 === 'HEAD) || (coins._2 === 'HEAD)}
    ) should be(Rational(3, 4))

    val coin2Conditioned = bothCoinsModel.filter(RegionEqTuple2of2('TAIL)).map(_._1)

    coin2Conditioned.P('HEAD) should be(Rational(1, 2))
  
 }

  test("two independent d6") {

    implicit val eqInt: cats.kernel.Eq[Int] = spire.implicits.IntAlgebra

    val bothDieModel = prob.chain(die(6))(die(6))

    bothDieModel.P(
      { rolls: (Int, Int) => (rolls._1 === 1) }
    ) should be(Rational(1, 6))

    bothDieModel.P(
      { rolls: (Int, Int) => (rolls._1 !== 3) }
    ) should be(Rational(5, 6))

    bothDieModel.P(
      { rolls: (Int, Int) => (rolls._1 === 1) && (rolls._2 === 2)}
    ) should be(Rational(1, 36))
  }
}
