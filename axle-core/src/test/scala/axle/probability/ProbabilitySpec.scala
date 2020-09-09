package axle.probability

import org.scalatest.funsuite._
import org.scalatest.matchers.should.Matchers

import spire.math._
import axle.game.Dice._
import axle.probability._
import axle.algebra._
import axle.syntax.kolmogorov.kolmogorovOps
import axle.syntax.bayes.bayesOps

class ProbabilitySpec extends AnyFunSuite with Matchers {

  val head = Symbol("HEAD")
  val tail = Symbol("TAIL")

  val mcpt = ConditionalProbabilityTable.monadWitness[Rational]
  // implicit val rat = new spire.math.RationalAlgebra()

  test("two independent coins") {

    val fairCoin = ConditionalProbabilityTable[Symbol, Rational](
      Map(
        head -> Rational(1, 2),
        tail -> Rational(1, 2)))

    import cats.implicits._

    val bothCoinsModel = mcpt.flatMap(fairCoin)( c1 => mcpt.map(fairCoin)( c2 => (c1, c2) ))

    type TWOFLIPS = (Symbol, Symbol)

    bothCoinsModel.P(RegionIf[TWOFLIPS](_._1 == head) and RegionIf[TWOFLIPS](_._2 == head)) should be(Rational(1, 4))

    bothCoinsModel.P(RegionEq((head, head))) should be(Rational(1, 4))

    bothCoinsModel.P(RegionIf(_._1 == head)) should be(Rational(1, 2))

    bothCoinsModel.P(RegionIf[TWOFLIPS](_._1 == head) or RegionIf[TWOFLIPS](_._2 == head)) should be(Rational(3, 4))

    val coin2Conditioned = mcpt.map(bothCoinsModel.filter(RegionIf[TWOFLIPS](_._2 == tail)))(_._1)

    coin2Conditioned.P(RegionEq(head)) should be(Rational(1, 2))
  
 }

  test("two independent d6") {

    val d6 = die(6)
    val bothDieModel = mcpt.flatMap(d6)( r1 => mcpt.map(d6)( r2 => (r1, r2)) )

    bothDieModel.P(RegionIf(_._1 == 1)) should be(Rational(1, 6))

    bothDieModel.P(RegionNegate(RegionIf(_._1 == 3))) should be(Rational(5, 6))

    bothDieModel.P(RegionAnd(RegionIf(_._1 == 1), RegionIf(_._2 == 2))) should be(Rational(1, 36))
  }
}
