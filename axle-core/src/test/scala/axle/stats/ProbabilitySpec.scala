package axle.stats

import org.scalatest._

import spire.math._
import axle.game.Dice._
import axle.syntax.probabilitymodel._

class ProbabilitySpec extends FunSuite with Matchers {

  type CPTR[T] = ConditionalProbabilityTable[T, Rational]

  test("two independent coins") {

    val coin1 = coin()
    val coin2 = coin()

    import cats.syntax.all._

    val bothCoinsModel: CPTR[(Symbol, Symbol)] = for {
      a <- coin1: CPTR[Symbol]
      b <- coin2: CPTR[Symbol]
    } yield (a, b)

    bothCoinsModel.P(
      { coins: (Symbol, Symbol) => (coins._1 === 'HEAD) && (coins._2 === 'HEAD)}
    ) should be(Rational(1, 4))

    bothCoinsModel.P(('HEAD, 'HEAD)) should be(Rational(1, 4))

    bothCoinsModel.P(
      { coins: (Symbol, Symbol) => coins._1 === 'HEAD }
    ) should be(Rational(1, 2))

    bothCoinsModel.P(
      { coins: (Symbol, Symbol) => (coins._1 === 'HEAD) || (coins._2 === 'HEAD)}
    ) should be(Rational(3, 4))

    val coin2Conditioned = bothCoinsModel.|(
      { coins: (Symbol, Symbol) => coins._2 === 'TAIL },
      { coins: (Symbol, Symbol) => coins._1})

    coin2Conditioned.P('HEAD) should be(Rational(1, 2))
  
 }

  test("two independent d6") {

    val d6a = die(6)
    val d6b = die(6)

    import cats.syntax.all._

    val bothDieModel: CPTR[(Int, Int)] = for {
      a <- d6a: CPTR[Int]
      b <- d6b: CPTR[Int]
    } yield (a, b)

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
