package axle.game.ttt

object Strategies {

  def validateMoveInputT(input: String, state: TicTacToeState, ttt: TicTacToe): Either[String, TicTacToeMove] = {
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

}