package axle.game.ttt

import axle.game._
import Stream.cons
import scala.Either

case class InteractiveTicTacToePlayer(id: String, description: String = "human")
    extends TicTacToePlayer() {

  override def introduceGame(ttt: TicTacToe): Unit = {
    val intro = """
Tic Tac Toe
Moves are numbers 1-%s.""".format(ttt.numPositions)
    println(intro)
  }

  override def displayEvents(events: List[Event[TicTacToe]], ttt: TicTacToe): Unit = {
    println()
    println(events.map(_.displayTo(this, ttt)).mkString("  "))
    println()
  }

  override def endGame(state: TicTacToeState, ttt: TicTacToe): Unit = {
    println()
    println(state.displayTo(this, ttt))
    println()
    println(state.outcome(ttt).map(_.displayTo(this, ttt)))
    println()
  }

  def userInputStream(): Stream[String] = {
    print("Enter move: ")
    val num = scala.io.StdIn.readLine()
    println
    cons(num, userInputStream)
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
      TicTacToeMove(this, position, ttt.boardSize)
    }
  }

  def move(state: TicTacToeState, ttt: TicTacToe): (TicTacToeMove, TicTacToeState) = {
    println(state.displayTo(state.player, ttt))
    val move =
      userInputStream().
        map(input => {
          val validated = validateMoveInput(input, state, ttt)
          validated.left.map(println)
          validated
        }).
        find(_.isRight).get.
        right.toOption.get
    (move, state(move, ttt).get) // TODO .get
  }

}
