package axle.game.montyhall

import org.scalatest.funsuite._
import org.scalatest.matchers.should.Matchers

import spire.random.Generator.rng

import axle.probability._
import axle.game._
import axle.game.Strategies._

class MontyHallSpec extends AnyFunSuite with Matchers {

  import axle.game.montyhall.evGame._
  import axle.game.montyhall.evGameIO._

  implicit val rat = new spire.math.RationalAlgebra()

  val game = MontyHall()

  test("game has an intro message") {
    introMessage(game) should include("Monty")
  }

  test("random game produces moveStateStream") {
    moveStateStream(
      game,
      startState(game),
      _ => randomMove,
      rng).take(2) should have length 2
  }

  test("random game plays") {
    val endState = play(
      game,
      _ => randomMove,
      _ => (s: String) => axle.IO.printLine[cats.effect.IO](s),
      startState(game),
      false,
      rng)
    moves(game, endState) should have length 0
  }

  test("random game produces game stream") {
    val games = gameStream(
      game,
      _ => randomMove,
      _ => (s: String) => axle.IO.printLine[cats.effect.IO](s),
      startState(game),
      false,
      rng).take(2)
    games should have length 2
  }

  test("startFrom returns the start state") {
    val state = startState(game)
    val move = moves(game, state).head
    val nextState = applyMove(game, state, move)
    val newStart = startFrom(game, nextState).get
    moves(game, newStart) should have length 3
    outcome(game, state) should be(None)
  }

  test("masked-sate mover is the same as raw state move") {
    val state = startState(game)
    val move = moves(game, state).head
    val nextState = applyMove(game, state, move)
    moverM(game, state) should be(mover(game, state))
    moverM(game, nextState) should be(mover(game, nextState))
  }

  test("starting moves are three-fold, display to monty with 'something'") {

    val startingMoves = moves(game, startState(game))
    val mm = evGame.maskMove(game, startingMoves.head, game.contestant, game.monty)

    displayMoveTo(game, mm, game.contestant, game.monty) should include("placed")
    startingMoves should have length 3
  }

  test("move parser") {

    evGameIO.parseMove(game, "foo") should be(Left("foo is not a valid move.  Please select again"))

    evGameIO.parseMove(game, "car 1") should be(Right(PlaceCar(1)))
    evGameIO.parseMove(game, "car 2") should be(Right(PlaceCar(2)))
    evGameIO.parseMove(game, "car 3") should be(Right(PlaceCar(3)))
    evGameIO.parseMove(game, "pick 1") should be(Right(FirstChoice(1)))
    evGameIO.parseMove(game, "pick 2") should be(Right(FirstChoice(2)))
    evGameIO.parseMove(game, "pick 3") should be(Right(FirstChoice(3)))
    evGameIO.parseMove(game, "reveal 1") should be(Right(Reveal(1)))
    evGameIO.parseMove(game, "reveal 2") should be(Right(Reveal(2)))
    evGameIO.parseMove(game, "reveal 3") should be(Right(Reveal(3)))
    evGameIO.parseMove(game, "change") should be(Right(Change()))
    evGameIO.parseMove(game, "stay") should be(Right(Stay()))
  }

  test("move validator") {
    val firstMove = PlaceCar(1)
    val secondState = applyMove(game, startState(game), firstMove)

    evGameIO.parseMove(game, "pick 1").flatMap(move => evGame.isValid(game, secondState, move)).isRight should be(true)
    evGameIO.parseMove(game, "pick 3").flatMap(move => evGame.isValid(game, secondState, move)).isRight should be(true)
  }

}
