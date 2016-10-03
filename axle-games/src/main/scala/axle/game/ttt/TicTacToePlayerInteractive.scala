package axle.game.ttt

import axle.game._
import Stream.cons

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

  def isValidMove(num: String, state: TicTacToeState, ttt: TicTacToe): Boolean = {
    try {
      val i = num.toInt
      if (i >= 1 && i <= ttt.numPositions) {
        if (state(i).isEmpty) {
          true
        } else {
          println("That space is occupied.")
          false
        }
      } else {
        println("Please enter a number between 1 and " + ttt.numPositions)
        false
      }
    } catch {
      case e: Exception => {
        println(num + " is not a valid move.  Please select again")
        false
      }
    }
  }

  def move(state: TicTacToeState, ttt: TicTacToe): (TicTacToeMove, TicTacToeState) = {
    println(state.displayTo(state.player, ttt))
    val position = userInputStream().find(input => isValidMove(input, state, ttt)).map(_.toInt).get
    val move = TicTacToeMove(this, position, ttt.boardSize)
    (move, state(move, ttt).get) // TODO .get
  }

}
