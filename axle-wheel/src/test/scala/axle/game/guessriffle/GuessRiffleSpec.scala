package axle.game.guessriffle

import org.scalatest._

import org.scalacheck.Gen
import org.scalacheck.Arbitrary

import org.scalacheck.Prop
import org.scalacheck.Prop.forAll

import spire.math._
import spire.random.Generator.rng

import axle.stats._
import axle.game._
import axle.game.Strategies._

class GuessRiffleSpec extends FunSuite with Matchers {

  import axle.game.guessriffle.evGame._
  import axle.game.guessriffle.evGameIO._

  implicit val dist = axle.stats.rationalProbabilityDist

  val player = Player("P", "Player")

  val randomGame = GuessRiffle(
    player,
    randomMove,
    axle.ignore,
    axle.ignore)

  test("hard coded game") {
    import axle.game.cards._
    val s0 = startState(randomGame)
    val s1 = applyMove(randomGame, s0, Riffle())
    val s2 = applyMove(randomGame, s1, GuessCard(Card(Rank('K'), Suit('C'))))
    val s3 = applyMove(randomGame, s2, RevealAndScore())
    s3.numCorrect should be < (2)
    moves(randomGame, s1).length should be(52)
  }

  test("game define intro message") {

    introMessage(randomGame) should include("Guess Riffle Shuffle")
  }

  test("random game produce moveStateStream") {
    moveStateStream(randomGame, startState(randomGame), rng).take(3).length should be(3)
  }

  test("random game plays") {
    val endState = play(randomGame, startState(randomGame), false, rng)
    moves(randomGame, endState).length should be(0)
  }

  test("optimal player strategy gets better score") {

    val pGame = GuessRiffle(player, GuessRiffle.perfectOptionsPlayerStrategy, axle.ignore, axle.ignore)
    val endState = play(pGame, startState(pGame), false, rng)

    // Note non-zero (but astronomically small) chance of this failing despite correct implementation
    outcome(pGame, endState).get.numCorrect should be >(10)
  }

  test("random game produce game stream") {
    val games = gameStream(randomGame, startState(randomGame), false, rng).take(2)
    games.length should be(2)
  }

  test("post-riffle state display") {
    displayStateTo(randomGame, applyMove(randomGame, startState(randomGame), Riffle()), player) should include("with 52 cards remaining")
  }

  test("startFrom return the start state") {
    val state = startState(randomGame)
    val move = moves(randomGame, state).head
    val nextState = applyMove(randomGame, state, move)
    val newStart = startFrom(randomGame, nextState).get
    moves(randomGame, newStart).length should be(1)
    outcome(randomGame, state) should be(None)
  }

  test("masked-sate mover be the same as raw state mover") {
    val state = startState(randomGame)
    val move = moves(randomGame, state).head
    val nextState = applyMove(randomGame, state, move)
    moverM(randomGame, state) should be(mover(randomGame, state))
    moverM(randomGame, nextState) should be(mover(randomGame, nextState))
  }

  test("starting moves") {

    val startingMoves = moves(randomGame, startState(randomGame))

    displayMoveTo(randomGame, Option(startingMoves.head), player, player) should include("riffle")
    startingMoves.length should be(1)
  }

  // TODO interactive player produces messages

  def containsCorrectGuess(game: GuessRiffle, fromState: GuessRiffleState, moveDist: ConditionalProbabilityTable[GuessRiffleMove, Rational]): Boolean =
    mover(game, fromState).map( mover =>
      if( mover === player ) {
        moveDist.values.map { m => m match { case GuessCard(card) => card } } contains ( fromState.remaining.head )
      } else {
        true
      }
    ) getOrElse true


  import spire.random.Random
  import spire.random.Seed

  def alwaysHasChanceOfCorrectGuess(gr: GuessRiffle): Prop =
    forAll { (seed: Int) =>
      stateStreamMap(gr, startState(gr), containsCorrectGuess _, Random.generatorFromSeed(Seed(seed))) forall { _._2 }
    }

  test("perfectOptionsPlayerStrategy always has non-zero chance of guessing correctly") {

    val pGame = GuessRiffle(player, GuessRiffle.perfectOptionsPlayerStrategy, axle.ignore, axle.ignore)

    alwaysHasChanceOfCorrectGuess(pGame)
  }

  test("perfectOptionsPlayerStrategy's P(all correct) >> that of random mover") {

    // val rGame = GuessRiffle(player, randomMove, axle.ignore, axle.ignore)
    // val pGame = GuessRiffle(player, GuessRiffle.perfectOptionsPlayerStrategy, axle.ignore, axle.ignore)

    // // leverages the fact that s0 will be the same for both games. Not generally true
    // val s0 = startState(randomGame)

    1 should be(2)
  }

  test("perfectOptionsPlayerStrategy's Entropy >> that of random mover") {

    1 should be(2)
  }

  test("Successively invest resources from initial state until all states have no movers") {

    // build upon basic PM[State, V] => PM[State, V] function

    1 should be(2)
  }

}
