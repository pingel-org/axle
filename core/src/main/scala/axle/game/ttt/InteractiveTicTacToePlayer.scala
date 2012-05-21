package axle.game.ttt

import axle.game._
import scala.collection._

case class InteractiveTicTacToePlayer(game: TicTacToe, playerId: String, description: String = "the human")
  extends TicTacToePlayer(game, playerId, description) {

  val eventQueue = mutable.ListBuffer[Event]()

  def introduceGame(): Unit = {
    val intro = """
Tic Tac Toe
Moves are numbers 1-%s.""".format(game.numPositions)
    println(intro)
  }

  def endGame(): Unit = {
    displayEvents()
    println(game.state)
  }

  def notify(event: Event): Unit = {
    eventQueue.append(event)
  }

  def displayEvents(): Unit = {
    val info = eventQueue.map(_.displayTo(this)).mkString("  ")
    println(info)
    eventQueue.clear()
  }

  def chooseMove(): TicTacToeMove = {
    displayEvents()
    println(game.state)
    while (true) {
      print("Enter move: ")
      val num = readLine()
      println
      try {
        val i = num.toInt
        if (i >= 1 && i <= game.numPositions) {
          if (game.state.getBoardAt(i).isEmpty) {
            return TicTacToeMove(game, this, i)
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
