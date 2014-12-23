package axle.game.ttt

import axle.game._
import Stream.cons

case class InteractiveTicTacToePlayer(id: String, description: String = "human")(implicit ttt: TicTacToe)
  extends TicTacToePlayer() {

  override def introduceGame(): Unit = {
    val intro = """
Tic Tac Toe
Moves are numbers 1-%s.""".format(ttt.numPositions)
    println(intro)
  }

  override def displayEvents(events: List[Event[TicTacToe]]): Unit = {
    println()
    println(events.map(_.displayTo(this)).mkString("  "))
    println()
  }

  override def endGame(state: TicTacToeState): Unit = {
    println()
    println(state.displayTo(this))
    println()
    println(state.outcome.map(_.displayTo(this)))
    println()
  }

  def userInputStream(): Stream[String] = {
    print("Enter move: ")
    val num = scala.io.StdIn.readLine()
    println
    cons(num, userInputStream)
  }

  def isValidMove(num: String, state: TicTacToeState): Boolean = {
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

  def move(state: TicTacToeState): (TicTacToeMove, TicTacToeState) = {
    println(state.displayTo(state.player))
    val move = TicTacToeMove(this, userInputStream().find(input => isValidMove(input, state)).map(_.toInt).get)
    (move, state(move).get) // TODO .get
  }

}
