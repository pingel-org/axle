package axle.game.ttt

import axle.game._
import scala.collection._

case class InteractiveTicTacToePlayer(
  itttPlayerId: String,
  itttDescription: String = "the human")
  extends Player[TicTacToe](itttPlayerId, itttDescription) {

  val eventQueue = mutable.ListBuffer[Event]()

  override def introduceGame(game: TicTacToe): Unit = {
    val intro = """
Tic Tac Toe
Moves are numbers 1-%s.""".format(game.numPositions)
    println(intro)
  }

  override def endGame(state: State[TicTacToe]): Unit = {
    displayEvents()
    println(state)
  }

  override def notify(event: Event): Unit = {
    eventQueue += event
  }

  def displayEvents(): Unit = {
    val info = eventQueue.map(_.displayTo(this)).mkString("  ")
    println(info)
    eventQueue.clear()
  }

  def chooseMove(state: TicTacToeState, game: TicTacToe): Move[TicTacToe] = {
    displayEvents()
    println(state)
    while (true) {
      print("Enter move: ")
      val num = readLine()
      println
      try {
        val i = num.toInt
        if (i >= 1 && i <= game.numPositions) {
          if (state.getBoardAt(i).isEmpty) {
            return TicTacToeMove(this, i, game)
          } else {
            println("That space is occupied.")
          }
        } else {
          println("Please enter a number between 1 and " + game.numPositions)
        }
      } catch {
        case e: Exception => {
          println(num + " is not a valid move.  Please select again")
        }
      }

    }
    null
  }

}
