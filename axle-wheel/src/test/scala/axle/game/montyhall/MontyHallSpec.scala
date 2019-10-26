package axle.game.montyhall

import org.scalatest._

import spire.random.Generator.rng
import axle.game._
import axle.game.Strategies._

class MontyHallSpec extends FunSuite with Matchers {

  import axle.game.montyhall.evGame._
  import axle.game.montyhall.evGameIO._

  implicit val dist = axle.stats.rationalProbabilityDist
  implicit val rat = new spire.math.RationalAlgebra()

  val contestant = Player("C", "Contestant")
  val monty = Player("M", "Monty Hall")

  val game = MontyHall(
    contestant, interactiveMove, axle.ignore,
    monty, interactiveMove, axle.ignore)

  val rGame = MontyHall(
    contestant, randomMove, axle.ignore,
    monty, randomMove, axle.ignore)

  test("random game has an intro message") {
    introMessage(rGame) should include("Monty")
  }

  test("random game produces moveStateStream") {
    moveStateStream(rGame, startState(rGame), rng).take(2) should have length 2
  }

  test("random game plays") {
    val endState = play(rGame, startState(rGame), false, rng)
    moves(rGame, endState) should have length 0
  }

  test("random game produces game stream") {
    val games = gameStream(rGame, startState(rGame), false, rng).take(2)
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
    val mm = evGame.maskMove(game, startingMoves.head, contestant, monty)

    displayMoveTo(game, mm, contestant, monty) should include("placed")
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

    evGameIO.parseMove(game, "pick 1").right.flatMap(move => evGame.isValid(game, secondState, move)).isRight should be(true)
    evGameIO.parseMove(game, "pick 3").right.flatMap(move => evGame.isValid(game, secondState, move)).isRight should be(true)
  }

}
