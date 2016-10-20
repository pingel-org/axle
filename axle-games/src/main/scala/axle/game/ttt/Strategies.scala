package axle.game.ttt

import axle.game._
import spire.math._

object Strategies {

  import util.Random.nextInt

  def randomMove(
    state: TicTacToeState,
    game: TicTacToe): TicTacToeMove = {
    val opens = state.moves(game).toList
    opens(nextInt(opens.length))
  }

  def validateMoveInput(input: String, state: TicTacToeState, ttt: TicTacToe): Either[String, TicTacToeMove] = {
    val eitherI: Either[String, Int] = try {
      val i: Int = input.toInt
      if (i >= 1 && i <= ttt.numPositions) {
        if (state(i).isEmpty) {
          Right(i)
        } else {
          Left("That space is occupied.")
        }
      } else {
        Left("Please enter a number between 1 and " + ttt.numPositions)
      }
    } catch {
      case e: Exception => {
        Left(input + " is not a valid move.  Please select again")
      }
    }
    eitherI.right.map { position =>
      TicTacToeMove(state.player, position, ttt.boardSize)
    }
  }

  def interactiveMove(
    state: TicTacToeState,
    ttt: TicTacToe): TicTacToeMove = {
    val display = ttt.playerToDisplayer(state.player)
    display(state.displayTo(state.player, ttt))
    userInputStream(display, axle.getLine).
      map(input => {
        val validated = validateMoveInput(input, state, ttt)
        validated.left.map(display)
        validated
      }).
      find(_.isRight).get.
      right.toOption.get
  }

  def heuristic(ttt: TicTacToe) = (state: TicTacToeState) => ttt.players.map(p => {
    (p, state.outcome(ttt).map(out => if (out.winner == Some(p)) Real(1) else Real(-1)).getOrElse(Real(0)))
  }).toMap

  def aiMover(lookahead: Int)(
    implicit evGame: Game[TicTacToe, TicTacToeState, TicTacToeOutcome, TicTacToeMove],
    evState: State[TicTacToe, TicTacToeState, TicTacToeOutcome, TicTacToeMove]) =
    (state: TicTacToeState, ttt: TicTacToe) => {
      val (move, newState, values) = minimax(ttt, state, lookahead, heuristic(ttt))
      move
    }

}