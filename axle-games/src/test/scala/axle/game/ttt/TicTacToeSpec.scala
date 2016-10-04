
package axle.game.ttt

import axle.game.scriptToLastMoveState
import org.specs2.mutable._

class TicTacToeSpec extends Specification {

  val game = TicTacToe(3, "human", "human")

  import game.{ x, o }

  def movesFrom(pps: List[(TicTacToePlayer, Int)]): List[TicTacToeMove] =
    pps.map({ case pp => TicTacToeMove(pp._1, pp._2, game.boardSize) })

  "game" should {
    "define intro message, have 9 positions" in {

      game.introMessage must contain("Intro")
      game.numPositions must be equalTo 9
    }
  }

  "start state" should {
    "display movement key to player x, and have 9 moves available to x" in {
      game.startState.displayTo(x, game) must contain("Movement Key")
    }
  }

  "startFrom" should {
    "simply return the start state" in {
      val state = game.startState
      val move = state.moves(game).head
      val nextState = state(move, game).get // TODO .get
      game.startFrom(nextState).get must be equalTo game.startState
    }
  }

  "starting moves" should {
    "be nine-fold, display to O with 'put an', and have string descriptions that contain 'upper'" in {

      val startingMoves = game.startState.moves(game)

      startingMoves.head.displayTo(o, game) must contain("put an")
      startingMoves.length must be equalTo 9
      startingMoves.map(_.description).mkString(",") must contain("upper")
    }
  }

  "event queues" should {
    "be two" in {
      val move = game.startState.moves(game).head
      val newState = game.startState.broadcast(game.players, move)
      newState.eventQueues.size must be equalTo 2
    }
  }

  "7-move x diagonal" should {
    "be a victory for x" in {
      val moves = movesFrom(List((x, 1), (o, 2), (x, 3), (o, 4), (x, 5), (o, 6), (x, 7)))
      val (_, lastState) = scriptToLastMoveState[TicTacToe](game, moves)
      val outcome = lastState.outcome(game).get
      outcome.displayTo(x, game) must contain("You have beaten")
      outcome.displayTo(o, game) must contain("beat you")
      outcome.winner.get should be equalTo x
    }
  }

  "7-move o diagonal" should {
    "be a victory for o" in {
      val moves = movesFrom(List((x, 2), (o, 3), (x, 4), (o, 5), (x, 6), (o, 7), (x, 8)))
      val (_, lastState) = scriptToLastMoveState[TicTacToe](game, moves)
      val winnerOpt = lastState.outcome(game).flatMap(_.winner)
      winnerOpt should be equalTo (Some(o))
    }
  }

  "9 move tie" should {
    "result in no-winner outcome" in {
      val moves = movesFrom(List((x, 1), (o, 2), (x, 3), (o, 4), (x, 5), (o, 7), (x, 8), (o, 9), (x, 6)))
      val (_, lastState) = scriptToLastMoveState[TicTacToe](game, moves)
      val winnerOpt = lastState.outcome(game).flatMap(_.winner)
      winnerOpt should be equalTo (None)
    }
  }

}
