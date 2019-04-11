package axle.game.guessriffle

import org.scalacheck.Properties
import org.scalacheck.Prop.forAllNoShrink

import edu.uci.ics.jung.graph.DirectedSparseGraph

import cats.syntax.all._

import spire.math._
import spire.random.Random
import spire.random.Seed
import spire.math.Rational

import axle.math.Π
import axle.math.Σ
import axle.game.Strategies._
import axle.game.guessriffle.evGame._
import axle.stats._
import axle.game._
import axle.quanta._
import axle.syntax.probabilitymodel._

class GuessRiffleProperties extends Properties("GuessRiffle Properties") {

  implicit val dist = axle.stats.rationalProbabilityDist

  def containsCorrectGuess(game: GuessRiffle, fromState: GuessRiffleState, moveDist: ConditionalProbabilityTable[GuessRiffleMove, Rational]): Boolean =
    mover(game, fromState).map( mover =>
      if( mover === game.player ) {
        val correctCard = fromState.remaining.head
        moveDist.p(GuessCard(correctCard)) > Rational(0)
      } else {
        true
      }
    ) getOrElse true

  property(s"perfectOptionsPlayerStrategy always has non-zero chance of guessing correctly") = {

    val player = Player("P", "Player")
    val pGame = GuessRiffle(player, GuessRiffle.perfectOptionsPlayerStrategy, axle.ignore, axle.ignore)

    forAllNoShrink { (seed: Int) =>
      stateStreamMap(pGame, startState(pGame), containsCorrectGuess _, Random.generatorFromSeed(Seed(seed)).sync ) forall { _._2 }
    }
  }

  def probabilityOfCorrectGuess(game: GuessRiffle, fromState: GuessRiffleState, moveDist: ConditionalProbabilityTable[GuessRiffleMove, Rational]): Option[Rational] =
    mover(game, fromState).map( mover =>
      if( mover === game.player ) {
        val correctCard = fromState.remaining.head
        Some(moveDist.P(GuessCard(correctCard)))
      } else {
        None
      }
    ) getOrElse None

  type CPTR[T] = ConditionalProbabilityTable[T, Rational]

  def entropyOfCorrectGuess(
    game: GuessRiffle,
    fromState: GuessRiffleState,
    moveDist: ConditionalProbabilityTable[GuessRiffleMove, Rational])(
    implicit
    infoConverterDouble: InformationConverter[Double]): Option[UnittedQuantity[Information, Double]] =
    mover(game, fromState).map( mover =>
      if( mover === game.player ) {
        val correctCard = fromState.remaining.head
        val isCorrectDist = (moveDist : CPTR[GuessRiffleMove]).map({ move => move === GuessCard(correctCard) })
        Some(entropy[ConditionalProbabilityTable, Boolean, Rational](isCorrectDist))
      } else {
        None
      }
    ) getOrElse None

  property("perfectOptionsPlayerStrategy's P(all correct) >> that of random mover (except when unshuffled), and its entropy is higher") = {

    implicit val doubleField: spire.algebra.Field[Double] = spire.implicits.DoubleAlgebra
    implicit val doubleOrder: cats.kernel.Order[Double] = spire.implicits.DoubleAlgebra
    implicit val infoConverterDouble: InformationConverter[Double] = {
      import axle.jung._
      Information.converterGraphK2[Double, DirectedSparseGraph]
    }

    val player = Player("P", "Player")
    val pGame = GuessRiffle(player, GuessRiffle.perfectOptionsPlayerStrategy, axle.ignore, axle.ignore)
    val rGame = GuessRiffle(player, randomMove, axle.ignore, axle.ignore)

    // leverages the fact that s0 will be the same for both games. Not generally true
    val s0 = startState(rGame)
    val s1 = applyMove(rGame, s0, Riffle())

    forAllNoShrink { (seed: Int) =>

      val probsP = stateStreamMap(pGame, s1, probabilityOfCorrectGuess _, Random.generatorFromSeed(Seed(seed)).sync ).flatMap(_._2).toList
      val entropiesP = stateStreamMap(pGame, s1, entropyOfCorrectGuess _, Random.generatorFromSeed(Seed(seed)).sync ).flatMap(_._2).toList
      val pp = Π(probsP)
      val ep = Σ(entropiesP)

      val probsR = stateStreamMap(rGame, s1, probabilityOfCorrectGuess _, Random.generatorFromSeed(Seed(seed)).sync ).flatMap(_._2).toList
      val entropiesR = stateStreamMap(rGame, s1, entropyOfCorrectGuess _, Random.generatorFromSeed(Seed(seed)).sync ).flatMap(_._2).toList
      val pr = Π(probsR)
      val er = Σ(entropiesR)

      // TODO accumulate single CPT during traversal rather than having combine them here
      // TODO factor common stuff out of axiom
      // TODO entropy-based assertion
      // TODO exepected outcome
      // TODO visualization

      (s1.initialDeck === s1.riffledDeck.get && pp === pr && ep === er) || {
        // println(s"pp = $pp, pr = $pr, ep = $ep, er = $er")
        (pp > pr) // && (ep > er)
      }
    }
  }

//   property("Successively invest resources from initial state until all states have no movers") = {
//     // build upon basic PM[State, V] => PM[State, V] function
//     // will require a better rational probability distribution as probabilities become smaller
//   }

}
