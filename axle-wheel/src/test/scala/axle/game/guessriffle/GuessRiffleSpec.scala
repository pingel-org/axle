package axle.game.guessriffle

import org.scalatest.funsuite._
import org.scalatest.matchers.should.Matchers

//import org.scalacheck.Gen
//import org.scalacheck.Arbitrary

import spire.random.Generator.rng

import axle.probability._
import axle.game._
import axle.game.Strategies._

class GuessRiffleSpec extends AnyFunSuite with Matchers {

  import axle.game.guessriffle.evGame._
  import axle.game.guessriffle.evGameIO._

  val player = Player("P", "Player")

  val randomGame = GuessRiffle(
    player,
    randomMove,
    axle.algebra.ignore,
    axle.algebra.ignore)

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

    val pGame = GuessRiffle(player, GuessRiffle.perfectOptionsPlayerStrategy, axle.algebra.ignore, axle.algebra.ignore)
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

}
