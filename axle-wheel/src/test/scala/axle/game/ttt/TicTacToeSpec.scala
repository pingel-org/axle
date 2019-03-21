package axle.game.ttt

import org.scalatest._

//import cats.implicits._

import spire.random.Generator.rng
import spire.math.Rational
import spire.algebra._

import axle.stats.ProbabilityModel
import axle.stats.ConditionalProbabilityTable0
import axle.game._
import axle.game.Strategies._

class TicTacToeSpec extends FunSuite with Matchers {

  val prob = implicitly[ProbabilityModel[ConditionalProbabilityTable0]]
  implicit val monad = implicitly[cats.Monad[({ type λ[T] = ConditionalProbabilityTable0[T, Rational] })#λ]]

  import axle.game.ttt.evGame._
  import axle.game.ttt.evGameIO._

  implicit val dist = axle.stats.rationalProbabilityDist

  val x = Player("X", "Player X")
  val o = Player("O", "Player O")

  val game = TicTacToe(
    3,
    x, interactiveMove, axle.ignore,
    o, interactiveMove, axle.ignore)

  test("game define intro message, have 9 positions") {

    introMessage(game) should include("Moves are")
    game.numPositions should be(9)
  }

  val rGame = TicTacToe(
    3,
    x, randomMove, axle.ignore,
    o, randomMove, axle.ignore)

  test("random game produce moveStateStream") {
    moveStateStream(rGame, startState(rGame), rng).take(3).length should be(3)
  }

  test("random game plays") {
    val endState = play(rGame, startState(rGame), false, rng)
    moves(rGame, endState).length should be(0)
  }

  test("random game produce game stream") {
    val games = gameStream(rGame, startState(rGame), false, rng).take(2)
    games.length should be(2)
  }

  test("start state display movement key to player x, and have 9 moves available to x") {
    displayStateTo(game, startState(game), x) should include("Movement Key")
  }

  test("startFrom return the start state") {
    val state = startState(game)
    val move = moves(game, state).head
    val nextState = applyMove(game, state, move)
    val newStart = startFrom(game, nextState).get
    moves(game, newStart).length should be(9)
    outcome(game, state) should be(None)
  }

  test("masked-sate mover be the same as raw state mover") {
    val state = startState(game)
    val move = moves(game, state).head
    val nextState = applyMove(game, state, move)
    moverM(game, state) should be(mover(game, state))
    moverM(game, nextState) should be(mover(game, nextState))
  }

  test("starting moves are nine-fold, display to O with 'put an', and have string descriptions that include 'upper'") {

    val startingMoves = moves(game, startState(game))

    displayMoveTo(game, startingMoves.head, x, o) should include("put an")
    startingMoves.length should be(9)
    startingMoves.map(_.description).mkString(",") should include("upper")
  }

  test("starting moves are defined for 4x4 game") {
    val bigGame = TicTacToe(
      4,
      x, randomMove, axle.ignore,
      o, randomMove, axle.ignore)
    val startingMoves = moves(bigGame, startState(bigGame))
    startingMoves.map(_.description).mkString(",") should include("16")
  }

  test("interactive player produces messages") {

    val firstMove = TicTacToeMove(2, game.boardSize)
    val secondState = applyMove(game, startState(game), firstMove)

    // val m = secondState.moverOpt.get
    evGameIO.parseMove(game, "14") should be(Left("Please enter a number between 1 and 9"))
    evGameIO.parseMove(game, "foo") should be(Left("foo is not a valid move.  Please select again"))

    evGameIO.parseMove(game, "1").right.flatMap(move => evGame.isValid(game, secondState, move)).right.toOption.get.position should be(1)
    evGameIO.parseMove(game, "2").right.flatMap(move => evGame.isValid(game, secondState, move)) should be(Left("That space is occupied."))
  }

  test("random strategy makes a move") {

    implicit val rat = new spire.math.RationalAlgebra()
    implicit val dist = axle.stats.rationalProbabilityDist

    val mover = randomMove
    val moveCpt = mover(game, startState(game))
    val m = prob.observe(moveCpt, rng)

    m.position should be > 0
  }

  test("A.I. strategy makes a move") {

    implicit val dist = axle.stats.rationalProbabilityDist
    import cats.implicits._ // for Order[Double]

    val firstMove = TicTacToeMove(2, game.boardSize)

    val h = (outcome: TicTacToeOutcome, p: Player) =>
      outcome.winner.map(wp => if (wp == p) 1d else -1d).getOrElse(0d)

    implicit val fieldDouble: Field[Double] = spire.implicits.DoubleAlgebra
    val ai4 = aiMover[TicTacToe, TicTacToeState, TicTacToeOutcome, TicTacToeMove, TicTacToeState, TicTacToeMove, Rational, Double](
      4, outcomeRingHeuristic(game, h))

    val secondState = applyMove(game, startState(game), firstMove)
    val cpt = ai4(game, secondState)
    val move = prob.observe(cpt, rng)

    move.position should be > 0
  }

  test("7-move x diagonal be a victory for x") {

    def xMove(game: TicTacToe, state: TicTacToeState): String =
      moves(game, state).size match {
        case 9 => "1"
        case 7 => "3"
        case 5 => "5"
        case 3 => "7"
      }

    def oMove(game: TicTacToe, state: TicTacToeState): String =
      moves(game, state).size match {
        case 8 => "2"
        case 6 => "4"
        case 4 => "6"
      }

    val game = TicTacToe(
      3,
      x, hardCodedStringStrategy(xMove), axle.ignore,
      o, hardCodedStringStrategy(oMove), axle.ignore)

    val start = startState(game)
    val lastState = moveStateStream(game, start, rng).last._3
    val out = outcome(game, lastState).get
    displayOutcomeTo(game, out, x) should include("You beat")
    displayOutcomeTo(game, out, o) should include("beat You")
    out.winner.get should be(x)
  }

  test("7-move o diagonal is a victory for o") {

    def xMove(game: TicTacToe, state: TicTacToeState): String =
      moves(game, state).size match {
        case 9 => "2"
        case 7 => "4"
        case 5 => "6"
        case 3 => "8"
      }

    def oMove(game: TicTacToe, state: TicTacToeState): String =
      moves(game, state).size match {
        case 8 => "3"
        case 6 => "5"
        case 4 => "7"
      }

    val game = TicTacToe(
      3,
      x, hardCodedStringStrategy(xMove), axle.ignore,
      o, hardCodedStringStrategy(oMove), axle.ignore)

    val start = startState(game)
    val lastState = moveStateStream(game, start, rng).last._3
    val winnerOpt = outcome(game, lastState).flatMap(_.winner)
    winnerOpt should be(Some(o))
  }

  test("9 move tie result in no-winner outcome") {

    def xMove(game: TicTacToe, state: TicTacToeState): String =
      moves(game, state).size match {
        case 9 => "1"
        case 7 => "3"
        case 5 => "5"
        case 3 => "8"
        case 1 => "6"
      }

    def oMove(game: TicTacToe, state: TicTacToeState): String =
      moves(game, state).size match {
        case 8 => "2"
        case 6 => "4"
        case 4 => "7"
        case 2 => "9"
      }

    val game = TicTacToe(
      3,
      x, hardCodedStringStrategy(xMove), axle.ignore,
      o, hardCodedStringStrategy(oMove), axle.ignore)

    val start = startState(game)
    val lastState = moveStateStream(game, start, rng).last._3

    val winnerOpt = outcome(game, lastState).flatMap(_.winner)
    winnerOpt should be(None)
  }

}
