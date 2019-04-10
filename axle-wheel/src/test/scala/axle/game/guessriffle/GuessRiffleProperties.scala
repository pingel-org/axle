package axle.game.guessriffle

// import org.scalatest._

//import org.scalacheck.Arbitrary
import org.scalacheck.Properties
import org.scalacheck.Prop
import org.scalacheck.Prop.forAllNoShrink

import spire.math._

import spire.math.Rational
import axle.stats._

import axle.game._
// import axle.game.Strategies._

class GuessRiffleProperties extends Properties("GuessRiffle Properties") {

  import cats.syntax.all._
  import axle.game.guessriffle.evGame._
  //import axle.game.guessriffle.evGameIO._

  implicit val dist = axle.stats.rationalProbabilityDist

  def containsCorrectGuess(game: GuessRiffle, fromState: GuessRiffleState, moveDist: ConditionalProbabilityTable[GuessRiffleMove, Rational]): Boolean =
    mover(game, fromState).map( mover =>
      if( mover === game.player ) {
        val guessableCards = moveDist.values.flatMap { m => m match {
           case GuessCard(card) => Some(card)
           case _ => { println(s"!!! non-GuessCard move: $m"); None }
        }}
        val correctCard = fromState.remaining.head
        guessableCards contains correctCard
      } else {
        true
      }
    ) getOrElse true


  import spire.random.Random
  import spire.random.Seed

  def alwaysHasChanceOfCorrectGuess(gr: GuessRiffle): Prop =
    forAllNoShrink { (seed: Int) =>
      stateStreamMap(gr, startState(gr), containsCorrectGuess _, Random.generatorFromSeed(Seed(seed % 1000000)).sync ) forall { _._2 }
    }

   property(s"perfectOptionsPlayerStrategy always has non-zero chance of guessing correctly") = {
     val player = Player("P", "Player")
     val pGame = GuessRiffle(player, GuessRiffle.perfectOptionsPlayerStrategy, axle.ignore, axle.ignore)
     alwaysHasChanceOfCorrectGuess(pGame)
   }


   property("perfectOptionsPlayerStrategy's P(all correct) >> that of random mover") = {

    // val rGame = GuessRiffle(player, randomMove, axle.ignore, axle.ignore)
    // val pGame = GuessRiffle(player, GuessRiffle.perfectOptionsPlayerStrategy, axle.ignore, axle.ignore)

    // // leverages the fact that s0 will be the same for both games. Not generally true
    // val s0 = startState(randomGame)

    ???
  }

  property("perfectOptionsPlayerStrategy's Entropy >> that of random mover") = {

    ???
  }

  property("Successively invest resources from initial state until all states have no movers") = {
    // build upon basic PM[State, V] => PM[State, V] function

    ???
  }

}
