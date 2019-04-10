package axle.game.guessriffle

import org.scalacheck.Properties
import org.scalacheck.Prop.forAllNoShrink

import cats.syntax.all._

import spire.math._
import spire.random.Random
import spire.random.Seed
import spire.math.Rational

import axle.math.Π
import axle.game.Strategies._
import axle.game.guessriffle.evGame._
//import axle.game.guessriffle.evGameIO._
import axle.stats._
import axle.game._
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

  property("perfectOptionsPlayerStrategy's P(all correct) >> that of random mover") = {

    val player = Player("P", "Player")
    val pGame = GuessRiffle(player, GuessRiffle.perfectOptionsPlayerStrategy, axle.ignore, axle.ignore)
    val rGame = GuessRiffle(player, randomMove, axle.ignore, axle.ignore)

    // // leverages the fact that s0 will be the same for both games. Not generally true
    val s0 = startState(rGame)

    forAllNoShrink { (seed: Int) =>
        val probsP = stateStreamMap(pGame, s0, probabilityOfCorrectGuess _, Random.generatorFromSeed(Seed(seed)).sync ).flatMap(_._2)
        val probsR = stateStreamMap(rGame, s0, probabilityOfCorrectGuess _, Random.generatorFromSeed(Seed(seed)).sync ).flatMap(_._2)
        Π(probsP.toList) > Π(probsR.toList)
    }
  }

//   property("perfectOptionsPlayerStrategy's Entropy >> that of random mover") = {
//     ???
//   }

//   property("Successively invest resources from initial state until all states have no movers") = {
//     // build upon basic PM[State, V] => PM[State, V] function
//     ???
//   }

}
