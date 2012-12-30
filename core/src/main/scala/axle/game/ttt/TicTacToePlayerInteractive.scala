package axle.game.ttt

import axle.game._
import collection._

class InteractiveTicTacToePlayer(itttPlayerId: String, itttDescription: String = "human")(implicit ttt: TicTacToe)
  extends TicTacToePlayer(itttPlayerId, itttDescription) {

  val eventQueue = mutable.ListBuffer[Event[TicTacToe]]()

  override def introduceGame(): Unit = {
    val intro = """
Tic Tac Toe
Moves are numbers 1-%s.""".format(ttt.numPositions)
    println(intro)
  }

  override def notify(event: Event[TicTacToe]): Unit = {
    eventQueue += event
  }

  override def displayEvents(): Unit = {
    val info = eventQueue.map(_.displayTo(this)).mkString("  ")
    println(info)
    eventQueue.clear()
  }

  def userInputStream(): Stream[String] = {
    print("Enter move: ")
    val num = readLine()
    println
    Stream.cons(num, userInputStream)
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
    displayEvents()
    println(state.displayTo(state.player))
    val move = TicTacToeMove(this, userInputStream().find(input => isValidMove(input, state)).map(_.toInt).get)
    (move, state(move).get) // TODO .get
  }

}
