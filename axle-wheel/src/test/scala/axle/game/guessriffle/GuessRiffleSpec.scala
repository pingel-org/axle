package axle.game.guessriffle

import org.scalatest.funsuite._
import org.scalatest.matchers.should.Matchers

import cats.implicits._

import spire.math._
import spire.random.Generator.rng

import axle.probability._
import axle.game._
import axle.game.Strategies._

class GuessRiffleSpec extends AnyFunSuite with Matchers {

  import axle.game.guessriffle.evGame
  import axle.game.guessriffle.evGame._
  import axle.game.guessriffle.evGameIO._

  val player = Player("P", "Player")

  val game = GuessRiffle(player)

  val rm = randomMove[GuessRiffle, GuessRiffleState, GuessRiffleOutcome, GuessRiffleMove, GuessRiffleState, Option[GuessRiffleMove], Rational, ConditionalProbabilityTable](game).andThen(Option.apply _)

  test("hard coded game") {
    import axle.game.cards._
    val s0 = startState(game)
    val s1 = applyMove(game, s0, Riffle())
    val s2 = applyMove(game, s1, GuessCard(Card(Rank('K'), Suit('C'))))
    val s3 = applyMove(game, s2, RevealAndScore())
    s3.numCorrect should be < (2)
    moves(game, s1).length should be(52)
  }

  test("game define intro message") {

    introMessage(game) should include("Guess Riffle Shuffle")
  }

  test("random game produce moveStateStream") {
    moveStateStream(
      game,
      startState(game),
      _ => rm,
      rng).get.take(3).length should be(3)
  }

  test("random game plays") {

    val endState = play(
      game,
      _ => rm,
      startState(game),
      rng).get

      moves(game, endState).length should be(0)
  }

  test("optimal player strategy gets better score") {

    val endState = play(
      game,
      player =>
        (if( player == GuessRiffle.dealer ) {
          GuessRiffle.dealerStrategy
        } else {
          GuessRiffle.perfectOptionsPlayerStrategy
        }).andThen(Option.apply _),
      startState(game),
      rng).get

    // Note non-zero (but astronomically small) chance of this failing despite correct implementation
    outcome(game, endState).get.numCorrect should be >(10)
  }

  test("random game produce game stream") {

    val games = gameStream(
      game,
      _ => rm,
      startState(game),
      i => i < 10,
      rng).get

    games.length should be(10)
  }

  test("post-riffle state display") {

    displayStateTo(game, applyMove(game, startState(game), Riffle()), player) should include("with 52 cards remaining")
  }

  test("startFrom return the start state") {

    val state = startState(game)
    val move = moves(game, state).head
    val nextState = applyMove(game, state, move)
    val newStart = startFrom(game, nextState).get

    moves(game, newStart).length should be(1)
    outcome(game, state) should be(None)
  }

  test("starting moves") {

    val startingMoves = moves(game, startState(game))

    displayMoveTo(game, Option(startingMoves.head), player, player) should include("riffle")
    startingMoves.length should be(1)
  }

  // TODO interactive player produces messages

}
