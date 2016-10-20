package axle.game.ttt

import axle.game._
import scala.Either

object InteractiveTicTacToePlayer {

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

  def move(
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

}
