
package axle.game.ttt

import axle.game._
import org.specs2.mutable._

class TicTacToeSpec extends Specification {

  val x = Player("X", "Player X")
  val o = Player("O", "Player O")

  val game = TicTacToe(3,
    x, interactiveMove, println,
    o, interactiveMove, println)

  def movesFrom(pps: List[(Player, Int)]): List[TicTacToeMove] =
    pps.map({ case pp => TicTacToeMove(pp._1, pp._2, game.boardSize) })

  "game" should {
    "define intro message, have 9 positions" in {

      introMessage(game) must contain("Moves are")
      game.numPositions must be equalTo 9
    }
  }

  "random game" should {

    val rGame = TicTacToe(3,
      x, randomMove, (s: String) => {},
      o, randomMove, (s: String) => {})

    "produce moveStateStream" in {
      moveStateStream(rGame, startState(rGame)).take(3).length must be equalTo 3
    }

    "play" in {
      val endState: TicTacToeState = play(rGame, startState(rGame), false).get
      // TODO number of moves should really be 0
      endState.moves(rGame).length must be lessThan 5
    }

    "product game stream" in {
      val games = gameStream(rGame, startState(rGame), false).take(2)
      games.length must be equalTo 2
    }

  }

  "start state" should {
    "display movement key to player x, and have 9 moves available to x" in {
      startState(game).displayTo(x, game) must contain("Movement Key")
    }
  }

  "startFrom" should {
    "simply return the start state" in {
      val state = startState(game)
      val move = state.moves(game).head
      val nextState = state(move, game)
      startFrom(game, nextState).get.moves(game).length must be equalTo 9
    }
  }

  "starting moves" should {
    "be nine-fold, display to O with 'put an', and have string descriptions that contain 'upper'" in {

      val startingMoves = startState(game).moves(game)

      evMove.displayTo(game, x, startingMoves.head, o) must contain("put an")
      startingMoves.length must be equalTo 9
      startingMoves.map(_.description).mkString(",") must contain("upper")
    }
    "be defined for 4x4 game" in {
      val bigGame = TicTacToe(4,
        x, randomMove, (s: String) => {},
        o, randomMove, (s: String) => {})
      val startingMoves = startState(bigGame).moves(game)
      startingMoves.map(_.description).mkString(",") must contain("16")
    }
  }

  "event queues" should {
    "be two-fold" in {
      val move = startState(game).moves(game).head
      val newState = broadcast(game, startState(game), Right(move))
      newState.eventQueues.size must be equalTo 2
    }
  }

  "interactive player" should {
    "print various messages" in {

      val firstMove = TicTacToeMove(x, 2, game.boardSize)
      val secondState = startState(game).apply(firstMove, game)

      introduceGame(x, game)
      displayEvents(game, x, List(Right(firstMove)))
      endGame(game, x, startState(game))

      val evGame = implicitly[Game[TicTacToe, TicTacToeState, TicTacToeOutcome, TicTacToeMove]]

      val m = secondState.player
      evGame.parseMove(game, "14", m) must be equalTo Left("Please enter a number between 1 and 9")
      evGame.parseMove(game, "foo", m) must be equalTo Left("foo is not a valid move.  Please select again")

      evGame.parseMove(game, "1", m).right.flatMap(move => evGame.isValid(game, secondState, move)).right.toOption.get.position must be equalTo 1
      evGame.parseMove(game, "2", m).right.flatMap(move => evGame.isValid(game, secondState, move)) must be equalTo Left("That space is occupied.")
    }
  }

  "random strategy" should {
    "make a move" in {

      val mover = randomMove
      val m = mover(startState(game), game)

      m.position must be greaterThan 0
    }
  }

  "A.I. strategy" should {
    "make a move" in {

      val firstMove = TicTacToeMove(x, 2, game.boardSize)

      import spire.implicits.DoubleAlgebra
      val ai4 = aiMover[TicTacToe, TicTacToeState, TicTacToeOutcome, TicTacToeMove, Double](4, didIWinHeuristic(game))

      val secondState = startState(game).apply(firstMove, game)

      val move = ai4(secondState, game)

      move.position must be greaterThan 0
    }
  }

  "7-move x diagonal" should {
    "be a victory for x" in {
      val moves = movesFrom(List((x, 1), (o, 2), (x, 3), (o, 4), (x, 5), (o, 6), (x, 7)))
      val (_, lastState) = scriptToLastMoveState(game, moves)
      val outcome = lastState.outcome(game).get
      evOutcome.displayTo(game, outcome, x) must contain("You beat")
      evOutcome.displayTo(game, outcome, o) must contain("beat You")
      outcome.winner.get should be equalTo x
    }
  }

  "7-move o diagonal" should {
    "be a victory for o" in {
      val moves = movesFrom(List((x, 2), (o, 3), (x, 4), (o, 5), (x, 6), (o, 7), (x, 8)))
      val (_, lastState) = scriptToLastMoveState(game, moves)
      val winnerOpt = lastState.outcome(game).flatMap(_.winner)
      winnerOpt should be equalTo (Some(o))
    }
  }

  "9 move tie" should {
    "result in no-winner outcome" in {
      val moves = movesFrom(List((x, 1), (o, 2), (x, 3), (o, 4), (x, 5), (o, 7), (x, 8), (o, 9), (x, 6)))
      val (_, lastState) = scriptToLastMoveState(game, moves)
      val winnerOpt = lastState.outcome(game).flatMap(_.winner)
      winnerOpt should be equalTo (None)
    }
  }

}
