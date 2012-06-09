package axle.game.ttt

import axle.game._
import scala.collection._

class InteractiveTicTacToePlayer(
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

  override def endGame(state: State[TicTacToe], game: TicTacToe): Unit = {
    displayEvents(game)
    println(state)
  }

  override def notify(event: Event): Unit = {
    eventQueue += event
  }

  def displayEvents(game: TicTacToe): Unit = {
    val info = eventQueue.map(_.displayTo(this.asInstanceOf[Player[Game]], game)).mkString("  ") // TODO cast
    println(info)
    eventQueue.clear()
  }

  def chooseMove(state: State[TicTacToe], game: TicTacToe): Move[TicTacToe] = {
    displayEvents(game)
    println(state)
    while (true) {
      print("Enter move: ")
      val num = readLine()
      println
      try {
        val i = num.toInt
        if (i >= 1 && i <= game.numPositions) {
          if (state.asInstanceOf[TicTacToeState].getBoardAt(i).isEmpty) { // TODO cast
            return TicTacToeMove(this, i)
          } else {
            println("That space is occupied.")
          }
        } else {
          println("Please enter a number between 1 and " + game.numPositions)
        }
      } catch {
        case e: Exception => println(num + " is not a valid move.  Please select again")
      }
    }
    null
  }

}
